package com.queuesetu.queuesetu.controller;

import com.queuesetu.queuesetu.dto.QueueDto;
import com.queuesetu.queuesetu.dto.QueueRequest;
import com.queuesetu.queuesetu.service.QueueClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@Tag(name = "Queue API", description = "Queue management proxied to queue service")
public class QueueController {

    @Autowired
    private QueueClientService queueClientService;

    @PostMapping
    @Operation(summary = "Create a new queue")
    public ResponseEntity<QueueDto> createQueue(@RequestBody QueueRequest body,
                                                HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(queueClientService.createQueue(body, authHeader));
    }

    @GetMapping("/{queueId}")
    @Operation(summary = "Get queue by ID")
    public ResponseEntity<QueueDto> getQueue(@PathVariable String queueId,
                                             HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        QueueDto queue = queueClientService.getQueue(queueId, authHeader);
        if (queue == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(queue);
    }

    @DeleteMapping("/{queueId}")
    @Operation(summary = "Delete queue by ID")
    public ResponseEntity<Void> deleteQueue(@PathVariable String queueId,
                                            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        queueClientService.deleteQueue(queueId, authHeader);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "List queues for a branch")
    public ResponseEntity<List<QueueDto>> getQueuesByBranch(@PathVariable String branchId,
                                                             HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(queueClientService.getQueuesByBranch(branchId, authHeader));
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "List queues for a tenant")
    public ResponseEntity<List<QueueDto>> getQueuesByTenant(@PathVariable String tenantId,
                                                             HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(queueClientService.getQueuesByTenant(tenantId, authHeader));
    }

    @GetMapping("/slot/{slotId}")
    @Operation(summary = "List queues for a slot")
    public ResponseEntity<List<QueueDto>> getQueuesBySlot(@PathVariable String slotId,
                                                          HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(queueClientService.getQueuesBySlot(slotId, authHeader));
    }
}
