package com.huawei.cloudnative.ecommerce.service;

import com.huawei.cloudnative.ecommerce.dto.LoginRequest;
import com.huawei.cloudnative.ecommerce.dto.LoginResponse;
import com.huawei.cloudnative.ecommerce.dto.RegisterRequest;
import com.huawei.cloudnative.ecommerce.entity.AppUser;
import com.huawei.cloudnative.ecommerce.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final TokenService tokenService;

    public AuthService(AppUserRepository appUserRepository, TokenService tokenService) {
        this.appUserRepository = appUserRepository;
        this.tokenService = tokenService;
    }

    public LoginResponse register(RegisterRequest request) {
        appUserRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new IllegalArgumentException("Username already exists");
        });

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(request.password());
        appUserRepository.save(user);

        String token = tokenService.createToken(user.getUsername());
        return new LoginResponse(token, user.getUsername());
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = tokenService.createToken(user.getUsername());
        return new LoginResponse(token, user.getUsername());
    }
}
