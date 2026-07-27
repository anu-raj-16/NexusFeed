package com.nexusfeed.nexus_backend.model;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    @Value("${JWT_SECRET}")
    private String secretKey;
}
