package com.queuesetu.queuesetu.controller;

import com.queuesetu.boot.core.user.UserDetail;
import com.queuesetu.queuesetu.dto.RoleAssignRequest;
import com.queuesetu.queuesetu.dto.RoleEntryDto;
import com.queuesetu.queuesetu.dto.UserSearchResult;
import com.queuesetu.queuesetu.service.UserClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * BFF role-management controller.
 *
 * <p>Permission rules:
 * <ul>
 *   <li>SA — can assign/remove any role</li>
 *   <li>TA(tenantId) — can assign/remove BRANCH_ADMIN, SERVICE_MANAGER, STAFF within that tenant</li>
 *   <li>BA(branchId) — can assign/remove SERVICE_MANAGER, STAFF within that branch</li>
 *   <li>SM(serviceId) — can assign/remove STAFF for queues under that service</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private UserClientService userClientService;

    // ── User search ───────────────────────────────────────────────────────────

    @GetMapping("/users/search")
    public ResponseEntity<List<UserSearchResult>> searchUsers(
            @RequestParam String email,
            HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        return ResponseEntity.ok(userClientService.searchUsers(email, auth));
    }

    // ── List role assignments ─────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<RoleEntryDto>> list(
            @RequestParam String scopeType,
            @RequestParam UUID scopeId,
            @RequestParam(required = false) String role,
            @AuthenticationPrincipal UserDetail principal,
            HttpServletRequest req) {

        checkCanView(principal, scopeType, scopeId, role);
        String auth = req.getHeader("Authorization");
        return ResponseEntity.ok(userClientService.listRoles(scopeType, scopeId, role, auth));
    }

    // ── Assign role ───────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<RoleEntryDto> assign(
            @RequestBody RoleAssignRequest request,
            @AuthenticationPrincipal UserDetail principal,
            HttpServletRequest req) {

        checkCanAssign(principal, request);
        String auth = req.getHeader("Authorization");
        return ResponseEntity.ok(userClientService.assignRole(request, auth));
    }

    // ── Revoke role ───────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(
            @PathVariable UUID id,
            @RequestParam String scopeType,
            @RequestParam UUID scopeId,
            @AuthenticationPrincipal UserDetail principal,
            HttpServletRequest req) {

        checkCanView(principal, scopeType, scopeId, null);
        String auth = req.getHeader("Authorization");
        userClientService.revokeRole(id, auth);
        return ResponseEntity.noContent().build();
    }

    // ── Permission checks ─────────────────────────────────────────────────────

    private void checkCanAssign(UserDetail p, RoleAssignRequest req) {
        if (p.isSuperAdmin()) return;

        String role = req.getRole() == null ? "" : req.getRole().toUpperCase();

        switch (role) {
            case "TENANT_ADMIN" -> {
                // Only SA can assign TENANT_ADMIN
                throw new AccessDeniedException("Only SuperAdmin can assign TenantAdmin role");
            }
            case "BRANCH_ADMIN" -> {
                // TA for that tenant can assign BA
                String tenantId = req.getTenantId() != null ? req.getTenantId().toString() : null;
                if (tenantId == null || !p.isTenantAdmin(tenantId)) {
                    throw new AccessDeniedException("Only TenantAdmin can assign BranchAdmin");
                }
            }
            case "SERVICE_MANAGER" -> {
                // TA or BA for that branch/tenant can assign SM
                String branchId = req.getBranchId() != null ? req.getBranchId().toString() : null;
                String tenantId = req.getTenantId() != null ? req.getTenantId().toString() : null;
                boolean allowed = (tenantId != null && p.isTenantAdmin(tenantId))
                        || (branchId != null && p.isBranchAdmin(branchId));
                if (!allowed) {
                    throw new AccessDeniedException("TenantAdmin or BranchAdmin required to assign ServiceManager");
                }
            }
            case "STAFF" -> {
                // TA, BA, or SM can assign STAFF
                String tenantId = req.getTenantId() != null ? req.getTenantId().toString() : null;
                String branchId = req.getBranchId() != null ? req.getBranchId().toString() : null;
                String serviceId = req.getServiceId() != null ? req.getServiceId().toString() : null;
                boolean allowed = (tenantId != null && p.isTenantAdmin(tenantId))
                        || (branchId != null && p.isBranchAdmin(branchId))
                        || (serviceId != null && p.isServiceManager(serviceId));
                if (!allowed) {
                    throw new AccessDeniedException("TenantAdmin, BranchAdmin, or ServiceManager required to assign Staff");
                }
            }
            default -> throw new AccessDeniedException("Unknown or unsupported role: " + role);
        }
    }

    private void checkCanView(UserDetail p, String scopeType, UUID scopeId, String role) {
        if (p.isSuperAdmin()) return;

        String scopeIdStr = scopeId.toString();
        boolean allowed = switch (scopeType.toUpperCase()) {
            case "TENANT" -> p.isTenantAdmin(scopeIdStr);
            case "BRANCH" -> p.isBranchAdmin(scopeIdStr) || !p.getIdsForRole("TA").isEmpty();
            case "SERVICE" -> p.isServiceManager(scopeIdStr)
                    || !p.getIdsForRole("BA").isEmpty()
                    || !p.getIdsForRole("TA").isEmpty();
            case "QUEUE" -> p.isStaffForQueue(scopeIdStr)
                    || !p.getIdsForRole("SM").isEmpty()
                    || !p.getIdsForRole("BA").isEmpty()
                    || !p.getIdsForRole("TA").isEmpty();
            default -> false;
        };

        if (!allowed) {
            throw new AccessDeniedException("Insufficient privileges to view roles for this scope");
        }
    }
}
