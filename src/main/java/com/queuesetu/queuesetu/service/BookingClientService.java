package com.queuesetu.queuesetu.service;

import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import queuesetu.ms.booking.dto.CreateServiceDefinitionRequest;
import queuesetu.ms.booking.dto.ServiceDefinition;
import queuesetu.ms.booking.dto.UpdateServiceDefinitionRequest;
import queuesetu.ms.booking.dto.CreateServiceSlotRequest;
import queuesetu.ms.booking.dto.ServiceSlot;
import queuesetu.ms.booking.dto.UpdateServiceSlotRequest;

import java.util.Arrays;
import java.util.List;

@Service
public class BookingClientService {

    private final RestClientFactory restClientFactory;
    private final String bookingServiceBaseUrl;

    public BookingClientService(RestClientFactory restClientFactory,
                                @Value("${services.booking.base-url}") String bookingServiceBaseUrl) {
        this.restClientFactory = restClientFactory;
        this.bookingServiceBaseUrl = bookingServiceBaseUrl;
    }

    public List<ServiceDefinition> getServicesByBranch(String branchId, String authHeader) {
        ServiceDefinition[] arr = restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .queryParam("branchId", branchId)
                .get("/api/services", ServiceDefinition[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public ServiceDefinition getService(String serviceId, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/services/" + serviceId, ServiceDefinition.class)
                .toEntity();
    }

    public ServiceDefinition createService(CreateServiceDefinitionRequest request, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/services", request, ServiceDefinition.class)
                .toEntity();
    }

    public ServiceDefinition updateService(String serviceId, UpdateServiceDefinitionRequest request, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .put("/api/services/" + serviceId, request, ServiceDefinition.class)
                .toEntity();
    }

    public void deleteService(String serviceId, String authHeader) {
        restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .delete("/api/services/" + serviceId, Void.class)
                .toEntity();
    }

    // ── Slot operations ───────────────────────────────────────────────────────

    public List<ServiceSlot> getSlotsByService(String serviceId, String authHeader) {
        ServiceSlot[] arr = restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .queryParam("serviceId", serviceId)
                .get("/api/slots", ServiceSlot[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public ServiceSlot getSlot(String slotId, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/slots/" + slotId, ServiceSlot.class)
                .toEntity();
    }

    public ServiceSlot createSlot(CreateServiceSlotRequest request, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/slots", request, ServiceSlot.class)
                .toEntity();
    }

    public ServiceSlot updateSlot(String slotId, UpdateServiceSlotRequest request, String authHeader) {
        return restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .put("/api/slots/" + slotId, request, ServiceSlot.class)
                .toEntity();
    }

    public void deleteSlot(String slotId, String authHeader) {
        restClientFactory.connect(bookingServiceBaseUrl)
                .header("Authorization", authHeader)
                .delete("/api/slots/" + slotId, Void.class)
                .toEntity();
    }
}
