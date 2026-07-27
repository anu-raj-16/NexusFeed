package com.nexusfeed.nexus_backend.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
