package com.nexusfeed.nexus_backend.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Entity
@Table(name = "resumes")
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "raw_text", columnDefinition = "TEXT")
    private String rawText;
    
    @Column(name = "vector_id")
    private String vectorId;
    
    @Column(name = "uploaded_at")
    private java.time.LocalDateTime uploadedAt;
}

