package com.nexusfeed.nexus_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusfeed.nexus_backend.model.AuthRequest;
import com.nexusfeed.nexus_backend.model.AuthResponse;
import com.nexusfeed.nexus_backend.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public AuthResponse signUp(@RequestParam String email, @RequestParam String password) {
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);
        return service.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestParam String email, @RequestParam String password) {
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);
        return service.login(request);
    }
}
