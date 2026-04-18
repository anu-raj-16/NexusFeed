package com.nexusfeed.nexus_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nexusfeed.nexus_backend.model.*;
import com.nexusfeed.nexus_backend.repository.JobRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    
    private final JobRepository jobRepo;

    JobController(JobRepository repo) {
        this.jobRepo = repo;
    }

    @GetMapping
    public List<Job> listAll(){
        return jobRepo.findAll();
    }

    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String keyword) {
        // We send the keyword twice: once for title, once for description
        return jobRepo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    @GetMapping("/{id}") // Matches /api/jobs/1
    public Job findById(@PathVariable Long id) {
        return jobRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id) {
        jobRepo.deleteById(id);
    }
}
