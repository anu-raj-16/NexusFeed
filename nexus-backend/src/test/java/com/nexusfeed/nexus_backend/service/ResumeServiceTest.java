package com.nexusfeed.nexus_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexusfeed.nexus_backend.model.JobMatch;
import com.nexusfeed.nexus_backend.model.Resume;

@ExtendWith(MockitoExtension.class)
public class ResumeServiceTest {

    @Test
    public void scoreJobs_returnsEmptyListWhenNoJobs() {
        ResumeService service = new ResumeService();
        Resume resume = new Resume();
        resume.setRawText("Python developer");

        List<JobMatch> results = service.scoreJobs(resume, List.of());

        assertEquals(0, results.size());
    }
}
