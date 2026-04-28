package com.queuesetu.queuesetu.service;

import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import queuesetu.ms.booking.dto.CreateServiceDefinitionRequest;
import queuesetu.ms.booking.dto.ServiceDefinition;
import queuesetu.ms.booking.dto.UpdateServiceDefinitionRequest;

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
}
