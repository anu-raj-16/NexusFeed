package com.nexusfeed.nexus_backend.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JobRepository {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    
}
