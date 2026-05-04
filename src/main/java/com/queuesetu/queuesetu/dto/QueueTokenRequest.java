package com.queuesetu.queuesetu.dto;

import java.util.UUID;

public class QueueTokenRequest {
    private UUID userId;
    private UUID appointmentId;
    private String type;
    private Integer tier;

    public QueueTokenRequest() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getAppointmentId() { return appointmentId; }
    public void setAppointmentId(UUID appointmentId) { this.appointmentId = appointmentId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getTier() { return tier; }
    public void setTier(Integer tier) { this.tier = tier; }
}
