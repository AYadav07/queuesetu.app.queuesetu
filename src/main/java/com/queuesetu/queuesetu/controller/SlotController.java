package com.queuesetu.queuesetu.controller;

import com.queuesetu.queuesetu.service.BookingClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import queuesetu.ms.booking.dto.CreateServiceSlotRequest;
import queuesetu.ms.booking.dto.ServiceSlot;
import queuesetu.ms.booking.dto.UpdateServiceSlotRequest;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@Tag(name = "Slot API", description = "Service slot management proxied to booking service")
public class SlotController {

    @Autowired
    private BookingClientService bookingClientService;

    @GetMapping
    @Operation(summary = "List slots for a service")
    public ResponseEntity<List<ServiceSlot>> getSlotsByService(@RequestParam String serviceId,
                                                               HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.getSlotsByService(serviceId, authHeader));
    }

    @PostMapping
    @Operation(summary = "Create a slot for a service")
    public ResponseEntity<ServiceSlot> createSlot(@RequestBody CreateServiceSlotRequest body,
                                                  HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.createSlot(body, authHeader));
    }

    @PutMapping("/{slotId}")
    @Operation(summary = "Update a slot by ID")
    public ResponseEntity<ServiceSlot> updateSlot(@PathVariable String slotId,
                                                  @RequestBody UpdateServiceSlotRequest body,
                                                  HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(bookingClientService.updateSlot(slotId, body, authHeader));
    }

    @DeleteMapping("/{slotId}")
    @Operation(summary = "Delete a slot by ID")
    public ResponseEntity<Void> deleteSlot(@PathVariable String slotId,
                                           HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        bookingClientService.deleteSlot(slotId, authHeader);
        return ResponseEntity.noContent().build();
    }
}
