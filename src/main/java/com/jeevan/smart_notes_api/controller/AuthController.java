package com.jeevan.smart_notes_api.controller;

import com.jeevan.smart_notes_api.dto.AuthResponse;
import com.jeevan.smart_notes_api.dto.LoginRequest;
import com.jeevan.smart_notes_api.dto.RefreshTokenRequest;
import com.jeevan.smart_notes_api.dto.RegisterRequest;
import com.jeevan.smart_notes_api.entity.User;
import com.jeevan.smart_notes_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {

        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return service.verify(request);
    }
    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return service.refreshToken(
                request.getRefreshToken()
        );
    }
    @PostMapping("/logout")
    public String logout(
            @RequestBody RefreshTokenRequest request) {

        return service.logout(
                request.getRefreshToken()
        );
    }
}