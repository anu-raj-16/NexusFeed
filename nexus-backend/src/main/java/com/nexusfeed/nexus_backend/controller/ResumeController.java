package com.nexusfeed.nexus_backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexusfeed.nexus_backend.model.Job;
import com.nexusfeed.nexus_backend.model.JobMatch;
import com.nexusfeed.nexus_backend.model.Resume;
import com.nexusfeed.nexus_backend.repository.JobRepository;
import com.nexusfeed.nexus_backend.repository.ResumeRepository;
import com.nexusfeed.nexus_backend.service.ResumeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeRepository resumeRepo;
    private final JobRepository jobRepo;
    private final ResumeService service;

    ResumeController(ResumeRepository repo, JobRepository jobRepo, ResumeService service) {
        this.resumeRepo = repo;
        this.jobRepo = jobRepo;
        this.service = service;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Resume resume = new Resume();
            resume.setRawText(text);
            resume.setUploadedAt(java.time.LocalDateTime.now());
            resume.setUserId(1L);
            Resume saved = resumeRepo.save(resume);
            return String.valueOf(saved.getId());
        } catch (IOException e) {
            return "Upload failed.";
        }
    }

    @GetMapping
    public List<Resume> listAll() {
        return resumeRepo.findAll();
    }

    @GetMapping("/{id}/match")
    public ResponseEntity<List<JobMatch>> matchEntity(@PathVariable Long id) {
        Optional<Resume> curr_resume = resumeRepo.findById(id);
        if (curr_resume.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Resume resume = curr_resume.get();
        List<Job> jobs = jobRepo.findAll();
        List<JobMatch> scoredJobs = service.scoreJobs(resume, jobs);
        return ResponseEntity.ok(scoredJobs);
    }
}
