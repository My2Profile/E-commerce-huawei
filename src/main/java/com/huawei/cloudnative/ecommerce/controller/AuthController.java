package com.huawei.cloudnative.ecommerce.controller;

import com.huawei.cloudnative.ecommerce.dto.LoginRequest;
import com.huawei.cloudnative.ecommerce.dto.LoginResponse;
import com.huawei.cloudnative.ecommerce.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        if (!"password123".equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenService.createToken(request.username());
        return ResponseEntity.ok(new LoginResponse(token, request.username()));
    }
}
