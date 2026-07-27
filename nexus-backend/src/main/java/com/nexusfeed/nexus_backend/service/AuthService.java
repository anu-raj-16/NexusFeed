package com.nexusfeed.nexus_backend.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexusfeed.nexus_backend.model.AuthRequest;
import com.nexusfeed.nexus_backend.model.AuthResponse;
import com.nexusfeed.nexus_backend.model.User;
import com.nexusfeed.nexus_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    AuthService(UserRepository userRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signup(AuthRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(java.time.LocalDateTime.now());
        userRepo.save(user);
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> user = userRepo.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        User foundUser = user.get();
        if (!passwordEncoder.matches(request.getPassword(), foundUser.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        String token = jwtService.generateToken(foundUser.getEmail());
        return new AuthResponse(token);
    }
}
