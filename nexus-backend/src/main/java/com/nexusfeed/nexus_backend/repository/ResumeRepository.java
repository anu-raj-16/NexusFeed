package com.nexusfeed.nexus_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexusfeed.nexus_backend.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserId(Long userId);
    
}
