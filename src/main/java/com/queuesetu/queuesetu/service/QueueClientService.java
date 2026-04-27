package com.queuesetu.queuesetu.service;

import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import com.queuesetu.queuesetu.dto.QueueDto;
import com.queuesetu.queuesetu.dto.QueueRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class QueueClientService {

    private final RestClientFactory restClientFactory;
    private final String queueServiceBaseUrl;

    public QueueClientService(RestClientFactory restClientFactory,
                              @Value("${services.queue.base-url}") String queueServiceBaseUrl) {
        this.restClientFactory = restClientFactory;
        this.queueServiceBaseUrl = queueServiceBaseUrl;
    }

    public QueueDto createQueue(QueueRequest request, String authHeader) {
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .post("/api/queues", request, QueueDto.class)
                .toEntity();
    }

    public QueueDto getQueue(String queueId, String authHeader) {
        return restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/queues/" + queueId, QueueDto.class)
                .toEntity();
    }

    public void deleteQueue(String queueId, String authHeader) {
        restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .delete("/api/queues/" + queueId, Void.class)
                .toEntity();
    }

    public List<QueueDto> getQueuesByBranch(String branchId, String authHeader) {
        QueueDto[] arr = restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/queues/branch/" + branchId, QueueDto[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public List<QueueDto> getQueuesByTenant(String tenantId, String authHeader) {
        QueueDto[] arr = restClientFactory.connect(queueServiceBaseUrl)
                .header("Authorization", authHeader)
                .get("/api/queues/tenant/" + tenantId, QueueDto[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }
}
