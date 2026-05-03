package com.queuesetu.queuesetu.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class QueueTokenDto {
    private UUID id;
    private OffsetDateTime checkinTime;
    private UUID queueId;
    private UUID appointmentId;
    private UUID userId;
    private String type;
    private Integer tokenNumber;
    private Double priorityScore;
    private String status;

    public QueueTokenDto() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OffsetDateTime getCheckinTime() { return checkinTime; }
    public void setCheckinTime(OffsetDateTime checkinTime) { this.checkinTime = checkinTime; }
    public UUID getQueueId() { return queueId; }
    public void setQueueId(UUID queueId) { this.queueId = queueId; }
    public UUID getAppointmentId() { return appointmentId; }
    public void setAppointmentId(UUID appointmentId) { this.appointmentId = appointmentId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(Integer tokenNumber) { this.tokenNumber = tokenNumber; }
    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
