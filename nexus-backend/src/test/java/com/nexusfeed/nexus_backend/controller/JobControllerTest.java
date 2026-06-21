package com.nexusfeed.nexus_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Optional;

import com.nexusfeed.nexus_backend.model.Job;
import com.nexusfeed.nexus_backend.repository.JobRepository;

@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobRepository jobRepo;

    @Test
    public void getAllJobs_returnsEmptyList() throws Exception {
        when(jobRepo.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/jobs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getAllJobs_returnsList() throws Exception {
        Job j = new Job();
        j.setId(1L);
        j.setTitle("Software Engineer Intern");
        j.setDescription("Great opportunity");

        when(jobRepo.findAll()).thenReturn(List.of(j));

        mockMvc.perform(get("/api/jobs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Software Engineer Intern"));
    }

    @Test
    public void findById_returnsNotFoundWhenJobDoesNotExist() throws Exception {
        when(jobRepo.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/jobs/999"))
            .andExpect(status().isNotFound());
    }
}
