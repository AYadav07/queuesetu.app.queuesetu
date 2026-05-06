package com.queuesetu.queuesetu.dto;

import java.util.UUID;

public class RoleAssignRequest {

    private UUID userId;
    /** Role name: TENANT_ADMIN, BRANCH_ADMIN, SERVICE_MANAGER, STAFF */
    private String role;
    private UUID tenantId;
    private UUID branchId;
    private UUID serviceId;
    private UUID queueId;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }

    public UUID getQueueId() { return queueId; }
    public void setQueueId(UUID queueId) { this.queueId = queueId; }
}
