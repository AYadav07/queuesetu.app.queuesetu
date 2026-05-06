package com.queuesetu.queuesetu.service;

import com.queuesetu.boot.core.restclient.client.ApiRestClient;
import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import com.queuesetu.queuesetu.dto.RoleAssignRequest;
import com.queuesetu.queuesetu.dto.RoleEntryDto;
import com.queuesetu.queuesetu.dto.UserSearchResult;
import com.queuesetu.user.dto.LoginRequest;
import com.queuesetu.user.dto.LoginResponse;
import com.queuesetu.user.dto.TokenRefreshRequest;
import com.queuesetu.user.dto.TokenRefreshResponse;
import com.queuesetu.user.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserClientService {

    private static final Logger log = LoggerFactory.getLogger(UserClientService.class);

    private final RestClientFactory restClientFactory;
    private final String userServiceBaseUrl;

    public UserClientService(RestClientFactory restClientFactory,
                             @Value("${services.user.base-url}") String userServiceBaseUrl) {
        this.restClientFactory = restClientFactory;
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    public User signUp(User request) {
        log.info("[BFF] Signing up user: {}", request.getEmail());
        return restClientFactory.connect(userServiceBaseUrl)
                .post("/api/auth/sign-up", request, User.class)
                .toEntity();
    }

    public LoginResponse login(LoginRequest request) {
        log.info("[BFF] Login request for: {}", request.getEmail());
        return restClientFactory.connect(userServiceBaseUrl)
                .post("/api/auth/login", request, LoginResponse.class)
                .toEntity();
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        return restClientFactory.connect(userServiceBaseUrl)
                .post("/api/auth/refresh", request, TokenRefreshResponse.class)
                .toEntity();
    }

    public User getCurrentUser(String authorizationHeader) {
        return restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .get("/api/auth/me", User.class)
                .toEntity();
    }

    public String logout(String authorizationHeader) {
        return restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .post("/api/auth/logout", null, String.class)
                .toEntity();
    }

    // ── User search ───────────────────────────────────────────────────────────

    public List<UserSearchResult> searchUsers(String emailPrefix, String authorizationHeader) {
        UserSearchResult[] arr = restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .queryParam("email", emailPrefix)
                .get("/api/auth/users/search", UserSearchResult[].class)
                .toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    // ── Role management ───────────────────────────────────────────────────────

    public RoleEntryDto assignRole(RoleAssignRequest request, String authorizationHeader) {
        log.info("[BFF] Assigning role {} to user {}", request.getRole(), request.getUserId());
        return restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .post("/api/roles", request, RoleEntryDto.class)
                .toEntity();
    }

    public void revokeRole(UUID assignmentId, String authorizationHeader) {
        log.info("[BFF] Revoking role assignment {}", assignmentId);
        restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .delete("/api/roles/" + assignmentId, Void.class)
                .toEntity();
    }

    public List<RoleEntryDto> listRoles(String scopeType, UUID scopeId, String role,
                                        String authorizationHeader) {
        ApiRestClient client = restClientFactory.connect(userServiceBaseUrl)
                .header("Authorization", authorizationHeader)
                .queryParam("scopeType", scopeType)
                .queryParam("scopeId", scopeId);
        if (role != null) {
            client.queryParam("role", role);
        }
        RoleEntryDto[] arr = client.get("/api/roles", RoleEntryDto[].class).toEntity();
        return arr != null ? Arrays.asList(arr) : List.of();
    }
}

