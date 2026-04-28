package com.queuesetu.queuesetu.controller;

import com.queuesetu.account.dto.Tenant;
import com.queuesetu.account.dto.TenantRequest;
import com.queuesetu.account.dto.Branch;
import com.queuesetu.queuesetu.service.AccountClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@Tag(name = "Tenant API", description = "Tenant management proxied to account service")
public class TenantController {

    @Autowired
    private AccountClientService accountClientService;

    @GetMapping("/my-tenants")
    @Operation(summary = "List tenants where the current user is TENANT_ADMIN")
    public ResponseEntity<List<Tenant>> getMyTenants(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.getMyTenants(authHeader));
    }

    @PostMapping
    @Operation(summary = "Create a new tenant")
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody TenantRequest body,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.createTenant(body, authHeader));
    }

    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant by ID")
    public ResponseEntity<Tenant> getTenant(@PathVariable String tenantId,
                                            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.getTenant(tenantId, authHeader));
    }

    @PutMapping("/{tenantId}")
    @Operation(summary = "Update tenant by ID")
    public ResponseEntity<Tenant> updateTenant(@PathVariable String tenantId,
                                               @Valid @RequestBody TenantRequest body,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.updateTenant(tenantId, body, authHeader));
    }

    @DeleteMapping("/{tenantId}")
    @Operation(summary = "Delete tenant by ID")
    public ResponseEntity<String> deleteTenant(@PathVariable String tenantId,
                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.deleteTenant(tenantId, authHeader));
    }

    @GetMapping("/{tenantId}/branches")
    @Operation(summary = "List branches for a tenant")
    public ResponseEntity<List<Branch>> getBranches(@PathVariable String tenantId,
                                                    HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(accountClientService.getBranchesByTenant(tenantId, authHeader));
    }
}
