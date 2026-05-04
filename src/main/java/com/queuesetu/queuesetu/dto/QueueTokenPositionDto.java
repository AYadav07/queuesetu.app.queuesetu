package com.queuesetu.queuesetu.dto;

import java.util.UUID;

public class QueueTokenPositionDto {
    private UUID queueId;
    private UUID userId;
    private UUID tokenId;
    private Long position;

    public QueueTokenPositionDto() {}

    public UUID getQueueId() { return queueId; }
    public void setQueueId(UUID queueId) { this.queueId = queueId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getTokenId() { return tokenId; }
    public void setTokenId(UUID tokenId) { this.tokenId = tokenId; }
    public Long getPosition() { return position; }
    public void setPosition(Long position) { this.position = position; }
}
