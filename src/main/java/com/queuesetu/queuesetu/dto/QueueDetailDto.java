package com.queuesetu.queuesetu.dto;

import java.util.List;
import java.util.UUID;

public class QueueDetailDto {
    private UUID id;
    private String name;
    private UUID tenantId;
    private UUID branchId;
    private UUID serviceId;
    private UUID counterId;
    private UUID slotId;

    private long totalTokens;
    private long waitingCount;
    private long calledCount;
    private long completedCount;

    private QueueTokenDto currentToken;
    private List<QueueTokenDto> nextTokens;

    public QueueDetailDto() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
    public long getTotalTokens() { return totalTokens; }
    public void setTotalTokens(long totalTokens) { this.totalTokens = totalTokens; }
    public long getWaitingCount() { return waitingCount; }
    public void setWaitingCount(long waitingCount) { this.waitingCount = waitingCount; }
    public long getCalledCount() { return calledCount; }
    public void setCalledCount(long calledCount) { this.calledCount = calledCount; }
    public long getCompletedCount() { return completedCount; }
    public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
    public QueueTokenDto getCurrentToken() { return currentToken; }
    public void setCurrentToken(QueueTokenDto currentToken) { this.currentToken = currentToken; }
    public List<QueueTokenDto> getNextTokens() { return nextTokens; }
    public void setNextTokens(List<QueueTokenDto> nextTokens) { this.nextTokens = nextTokens; }
}
