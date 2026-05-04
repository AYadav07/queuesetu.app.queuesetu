package com.queuesetu.queuesetu.controller;

import com.queuesetu.queuesetu.dto.QueueTokenDto;
import com.queuesetu.queuesetu.dto.QueueTokenPositionDto;
import com.queuesetu.queuesetu.dto.QueueTokenRequest;
import com.queuesetu.queuesetu.service.QueueTokenClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue-tokens")
@Tag(name = "Queue Token API", description = "Join a queue and check live position")
public class QueueTokenController {

    @Autowired
    private QueueTokenClientService queueTokenClientService;

    /**
     * Join a queue (create a token / check-in).
     * POST /api/queue-tokens/{queueId}/tokens
     */
    @PostMapping("/{queueId}/tokens")
    @Operation(summary = "Join a queue (create token)")
    public ResponseEntity<QueueTokenDto> joinQueue(
            @PathVariable String queueId,
            @RequestBody QueueTokenRequest body,
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        QueueTokenDto token = queueTokenClientService.joinQueue(queueId, body, authHeader);
        return ResponseEntity.ok(token);
    }

    /**
     * Get the live position of a user in a queue.
     * GET /api/queue-tokens/{queueId}/position/{userId}
     * Returns 204 if the user has no active token in this queue.
     */
    @GetMapping("/{queueId}/position/{userId}")
    @Operation(summary = "Get user's position in a queue")
    public ResponseEntity<QueueTokenPositionDto> getPosition(
            @PathVariable String queueId,
            @PathVariable String userId,
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        QueueTokenPositionDto pos = queueTokenClientService.getPosition(queueId, userId, authHeader);
        if (pos == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pos);
    }

    /**
     * Call the next waiting token in a queue (staff/admin only).
     * POST /api/queue-tokens/{queueId}/callNext
     * Returns 204 if the queue is empty.
     */
    @PostMapping("/{queueId}/callNext")
    @Operation(summary = "Call the next token (staff/admin)")
    // SA || any-TA || any-BA || any-SM || ST(queueId). MS enforces full-scope check.
    @PreAuthorize("@rbac.canOperateQueue(authentication, #queueId)")
    public ResponseEntity<QueueTokenDto> callNext(
            @PathVariable String queueId,
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        QueueTokenDto next = queueTokenClientService.callNext(queueId, authHeader);
        if (next == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(next);
    }

    /**
     * Mark a token as completed.
     * PATCH /api/queue-tokens/{queueId}/tokens/{tokenId}/complete
     */
    @PostMapping("/{queueId}/tokens/{tokenId}/complete")
    @Operation(summary = "Mark a token as completed (staff/admin)")
    // SA || any-TA || any-BA || any-SM || ST(queueId). MS enforces full-scope check.
    @PreAuthorize("@rbac.canOperateQueue(authentication, #queueId)")
    public ResponseEntity<QueueTokenDto> markCompleted(
            @PathVariable String queueId,
            @PathVariable String tokenId,
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        QueueTokenDto updated = queueTokenClientService.markCompleted(queueId, tokenId, authHeader);
        return ResponseEntity.ok(updated);
    }
}
