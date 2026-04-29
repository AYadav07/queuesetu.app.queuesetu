package com.queuesetu.queuesetu.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.queuesetu.boot.core.restclient.exception.RestClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        String service = resolveServiceName(req.getRequestURI());
        log.warn("[{}] Validation error at {}: {}", service, req.getRequestURI(), message);
        return ResponseEntity.badRequest().body(new ErrorResponse(message, 400));
    }

    /**
     * Handles errors from downstream microservices. Extracts the HTTP status and
     * message from the upstream response body so the client receives the real error.
     */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex,
                                                                   HttpServletRequest req) {
        String path = req.getRequestURI();
        String service = resolveServiceName(path);
        Throwable cause = ex.getCause();

        if (cause instanceof HttpStatusCodeException httpEx) {
            int status = httpEx.getStatusCode().value();
            String body = httpEx.getResponseBodyAsString();
            String message = extractMessage(body, ex.getMessage());

            if (status >= 500) {
                log.error("[{}] Upstream error at {} → {}", service, path, message);
            } else {
                log.warn("[{}] {} (HTTP {}) at {}", service, message, status, path);
            }
            return ResponseEntity.status(status).body(new ErrorResponse(message, status));
        }

        log.error("[{}] Service unreachable at {}: {}", service, path, ex.getMessage(), ex);
        return ResponseEntity.status(502).body(new ErrorResponse("Upstream service unavailable", 502));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest req) {
        String service = resolveServiceName(req.getRequestURI());
        log.error("[{}] Unexpected error at {}: {}", service, req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("An unexpected error occurred", 500));
    }

    /** Maps BFF route prefixes to their backing service name. */
    private String resolveServiceName(String path) {
        if (path == null) return "bff";
        if (path.startsWith("/api/auth"))          return "user";
        if (path.startsWith("/api/tenants")
                || path.startsWith("/api/branches")) return "account";
        if (path.startsWith("/api/services")
                || path.startsWith("/api/slots")
                || path.startsWith("/api/appointments")
                || path.startsWith("/api/counters"))  return "booking";
        if (path.startsWith("/api/queues")
                || path.startsWith("/api/queue-tokens")) return "queue";
        return "bff";
    }

    private String extractMessage(String responseBody, String fallback) {
        if (responseBody == null || responseBody.isBlank()) return fallback;
        try {
            JsonNode node = MAPPER.readTree(responseBody);
            if (node.has("message") && !node.get("message").isNull()) {
                return node.get("message").asText();
            }
            if (node.has("error") && !node.get("error").isNull()) {
                return node.get("error").asText();
            }
        } catch (Exception ignored) {
            // body is not JSON — return as-is if short enough
            if (responseBody.length() < 300) return responseBody;
        }
        return fallback;
    }
}
