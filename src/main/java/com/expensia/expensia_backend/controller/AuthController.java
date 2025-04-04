package com.expensia.expensia_backend.controller;

import com.expensia.expensia_backend.dto.AuthResponse;
import com.expensia.expensia_backend.dto.LoginRequest;
import com.expensia.expensia_backend.dto.RegisterRequest;
import com.expensia.expensia_backend.model.User;
import com.expensia.expensia_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginReques) {
        return authService.login(loginReques);
    }
}
