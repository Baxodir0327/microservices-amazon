package com.amazon.authservice.controller;

import com.amazon.authservice.payload.ApiResult;
import com.amazon.authservice.payload.LoginDTO;
import com.amazon.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ApiResult> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
    @PostMapping("/register")
    public Mono<String> register(@RequestBody LoginDTO loginDTO) {
        return authService.register(loginDTO);
    }
}
