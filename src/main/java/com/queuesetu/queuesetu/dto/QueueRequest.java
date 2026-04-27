package com.queuesetu.queuesetu.dto;

import java.util.UUID;

public class QueueRequest {
    private String name;
    private UUID tenantId;
    private UUID branchId;
    private UUID serviceId;
    private UUID counterId;
    private UUID slotId;

    public QueueRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public UUID getCounterId() { return counterId; }
    public void setCounterId(UUID counterId) { this.counterId = counterId; }
    public UUID getSlotId() { return slotId; }
    public void setSlotId(UUID slotId) { this.slotId = slotId; }
}
