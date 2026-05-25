package com.nexusfeed.nexus_backend.controller;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexusfeed.nexus_backend.model.Resume;
import com.nexusfeed.nexus_backend.repository.ResumeRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeRepository resumeRepo;

    ResumeController(ResumeRepository repo) {
        this.resumeRepo = repo;
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
            resumeRepo.save(resume);
            return "Resume uploaded!";
        } catch (IOException e) {
            return "Upload failed.";
        }
    }

    @GetMapping
    public List<Resume> listAll() {
        return resumeRepo.findAll();
    }
}
