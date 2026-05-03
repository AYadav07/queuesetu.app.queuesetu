package com.queuesetu.queuesetu.controller;

import com.queuesetu.queuesetu.service.BookingClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import queuesetu.ms.booking.dto.CreateServiceDefinitionRequest;
import queuesetu.ms.booking.dto.ServiceDefinition;
import queuesetu.ms.booking.dto.UpdateServiceDefinitionRequest;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Tag(name = "Service API", description = "Service management proxied to booking service")
public class ServiceController {

    @Autowired
    private BookingClientService bookingClientService;

    @GetMapping
    @Operation(summary = "List services for a branch")
    public ResponseEntity<List<ServiceDefinition>> getServicesByBranch(@RequestParam String branchId,
                                                                        HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.getServicesByBranch(branchId, authHeader));
    }

    @GetMapping("/{branchId}/services")
    @Operation(summary = "List of services for a branch")
    public ResponseEntity<List<ServiceDefinition>> getBranches(@PathVariable String branchId,
                                                    HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.getServicesByBranch(branchId, authHeader));
    }

    @GetMapping("/{serviceId}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ServiceDefinition> getService(@PathVariable String serviceId,
                                                        HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        ServiceDefinition svc = bookingClientService.getService(serviceId, authHeader);
        if (svc == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(svc);
    }

    @PostMapping
    @Operation(summary = "Create a service in a branch")
    public ResponseEntity<ServiceDefinition> createService(@RequestBody CreateServiceDefinitionRequest body,
                                                           HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.createService(body, authHeader));
    }

    @PutMapping("/{serviceId}")
    @Operation(summary = "Update a service by ID")
    public ResponseEntity<ServiceDefinition> updateService(@PathVariable String serviceId,
                                                           @RequestBody UpdateServiceDefinitionRequest body,
                                                           HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.updateService(serviceId, body, authHeader));
    }

    @DeleteMapping("/{serviceId}")
    @Operation(summary = "Delete a service by ID")
    public ResponseEntity<Void> deleteService(@PathVariable String serviceId,
                                              HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        bookingClientService.deleteService(serviceId, authHeader);
        return ResponseEntity.noContent().build();
    }
}
