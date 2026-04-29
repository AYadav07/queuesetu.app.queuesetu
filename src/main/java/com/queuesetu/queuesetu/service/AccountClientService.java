package com.queuesetu.queuesetu.service;

import com.queuesetu.account.dto.Branch;
import com.queuesetu.account.dto.BranchRequest;
import com.queuesetu.account.dto.Tenant;
import com.queuesetu.account.dto.TenantRequest;
import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AccountClientService {

    private static final Logger log = LoggerFactory.getLogger(AccountClientService.class);

    private final RestClientFactory restClientFactory;
    private final String accountServiceBaseUrl;

    public AccountClientService(RestClientFactory restClientFactory,
                                @Value("${services.account.base-url}") String accountServiceBaseUrl) {
        this.restClientFactory = restClientFactory;
        this.accountServiceBaseUrl = accountServiceBaseUrl;
    }

    // ── Tenant operations ─────────────────────────────────────────────────────

    public List<Tenant> getMyTenants(String authHeader) {
        log.info("[BFF] Fetching tenants from account service");
        Tenant[] arr = restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/tenant/my-tenants", Tenant[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public Tenant createTenant(TenantRequest request, String authHeader) {
        log.info("[BFF] Creating tenant '{}'", request.getTenantName());
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/tenant/add", request, Tenant.class)
                .toEntity();
    }

    public Tenant getTenant(String tenantId, String authHeader) {
        log.info("[BFF] Fetching tenant {}", tenantId);
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/tenant/" + tenantId, Tenant.class)
                .toEntity();
    }

    public Tenant updateTenant(String tenantId, TenantRequest request, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .put("/api/tenant/" + tenantId, request, Tenant.class)
                .toEntity();
    }

    public String deleteTenant(String tenantId, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .delete("/api/tenant/" + tenantId, String.class)
                .toEntity();
    }

    // ── Branch operations ─────────────────────────────────────────────────────

    public List<Branch> getBranchesByTenant(String tenantId, String authHeader) {
        Branch[] arr = restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/branch/" + tenantId + "/branches", Branch[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public Branch createBranch(BranchRequest request, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/branch/add", request, Branch.class)
                .toEntity();
    }

    public Branch getBranch(String branchId, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/branch/" + branchId, Branch.class)
                .toEntity();
    }

    public Branch updateBranch(String branchId, BranchRequest request, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .put("/api/branch/update/" + branchId, request, Branch.class)
                .toEntity();
    }

    public String deleteBranch(String branchId, String authHeader) {
        return restClientFactory.connect(accountServiceBaseUrl)
                .header("Authorization", authHeader)
                .delete("/api/branch/delete/" + branchId, String.class)
                .toEntity();
    }
}
