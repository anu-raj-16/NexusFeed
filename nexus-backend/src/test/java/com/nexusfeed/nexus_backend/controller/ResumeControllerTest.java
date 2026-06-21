package com.nexusfeed.nexus_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.nexusfeed.nexus_backend.model.Resume;
import com.nexusfeed.nexus_backend.repository.JobRepository;
import com.nexusfeed.nexus_backend.repository.ResumeRepository;
import com.nexusfeed.nexus_backend.service.ResumeService;

@WebMvcTest(ResumeController.class)
public class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResumeRepository resumeRepo;

    @MockitoBean
    private JobRepository jobRepo;

    @MockitoBean
    private ResumeService resumeService;

    @Test
    public void uploadResume_savesAndReturnsId() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "resume.pdf",
                                "application/pdf",
                                "fake pdf content".getBytes()
                            );
        Resume r = new Resume();
        r.setId(1L);
        // 2. Mock resumeRepo.save() to return a Resume with an ID
        when(resumeRepo.save(any(Resume.class))).thenReturn(r);
        // 3. Perform a multipart POST request
        mockMvc.perform(multipart("/api/resumes/upload").file(file))
    .andExpect(status().isOk())
    .andExpect(content().string("Upload failed."));
    }

    @Test
    public void matchEntity_returnsNotFoundWhenResumeDoesNotExist() throws Exception {
        when(resumeRepo.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/resumes/999/match"))
            .andExpect(status().isNotFound());
    }
}
