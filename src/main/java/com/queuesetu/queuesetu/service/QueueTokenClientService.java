package com.queuesetu.queuesetu.service;

import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import com.queuesetu.queuesetu.dto.QueueTokenDto;
import com.queuesetu.queuesetu.dto.QueueTokenPositionDto;
import com.queuesetu.queuesetu.dto.QueueTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueueTokenClientService {

    private static final Logger log = LoggerFactory.getLogger(QueueTokenClientService.class);

    private final RestClientFactory restClientFactory;
    private final String queueServiceBaseUrl;

    public QueueTokenClientService(RestClientFactory restClientFactory,
                                   @Value("${services.queue.base-url}") String queueServiceBaseUrl) {
        this.restClientFactory = restClientFactory;
        this.queueServiceBaseUrl = queueServiceBaseUrl;
    }

    /** POST /api/queue-tokens/{queueId}/tokens  →  join the queue */
    public QueueTokenDto joinQueue(String queueId, QueueTokenRequest request, String authHeader) {
        log.info("[BFF] User {} joining queue {}", request.getUserId(), queueId);
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/queue-tokens/" + queueId + "/tokens", request, QueueTokenDto.class)
                .toEntity();
    }

    /** GET /api/queue-tokens/{queueId}/position/{userId}  →  get live position */
    public QueueTokenPositionDto getPosition(String queueId, String userId, String authHeader) {
        log.info("[BFF] Fetching position for user {} in queue {}", userId, queueId);
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/queue-tokens/" + queueId + "/position/" + userId, QueueTokenPositionDto.class)
                .toEntity();
    }

    /** POST /api/queue-tokens/{queueId}/callNext  →  call the next waiting token */
    public QueueTokenDto callNext(String queueId, String authHeader) {
        log.info("[BFF] Calling next token for queue {}", queueId);
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/queue-tokens/" + queueId + "/callNext", null, QueueTokenDto.class)
                .toEntity();
    }

    /** POST /api/queue-tokens/{queueId}/tokens/{tokenId}/complete  →  mark token completed */
    public QueueTokenDto markCompleted(String queueId, String tokenId, String authHeader) {
        log.info("[BFF] Marking token {} as completed in queue {}", tokenId, queueId);
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/queue-tokens/" + queueId + "/tokens/" + tokenId + "/complete", null, QueueTokenDto.class)
                .toEntity();
    }
}
