package com.nexusfeed.nexus_backend.repository;

import com.nexusfeed.nexus_backend.model.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String desc);
    
}
