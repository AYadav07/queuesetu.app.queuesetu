package com.queuesetu.queuesetu.controller;

import com.queuesetu.queuesetu.service.UserClientService;
import com.queuesetu.user.dto.LoginRequest;
import com.queuesetu.user.dto.LoginResponse;
import com.queuesetu.user.dto.TokenRefreshRequest;
import com.queuesetu.user.dto.TokenRefreshResponse;
import com.queuesetu.user.dto.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Auth operations proxied to user service")
public class AuthController {

    @Autowired
    private UserClientService userClientService;

    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user")
    public ResponseEntity<User> signUp(@Valid @RequestBody User request) {
        return ResponseEntity.ok(userClientService.signUp(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userClientService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(userClientService.refreshToken(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(userClientService.getCurrentUser(authHeader));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout current user")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(userClientService.logout(authHeader));
    }
}

