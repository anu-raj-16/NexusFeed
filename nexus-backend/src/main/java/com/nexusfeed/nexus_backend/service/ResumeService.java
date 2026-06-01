package com.nexusfeed.nexus_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import com.nexusfeed.nexus_backend.model.Job;
import com.nexusfeed.nexus_backend.model.JobMatch;
import com.nexusfeed.nexus_backend.model.Resume;


@Service
public class ResumeService {

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    private RestClient restClient = RestClient.create();

    public List<JobMatch> scoreJobs(Resume resume, List<Job> jobs) {
        List<JobMatch> matches = new ArrayList<>();
        for (Job job: jobs) {
            String prompt = "You are a job matcher. Given a resume and a job description, " +
            "evaluate how well the candidate fits the role.\n\n" +
            "Resume:\n" + resume.getRawText() + "\n\n" +
            "Job Description:\n" + job.getDescription() + "\n\n" +
            "Return a JSON object with:\n" +
            "- score: a number (float) 0-100\n" +
            "- matched_skills: a list of skills (list string) that align\n" +
            "- gaps: a list (list string) of missing skills or experience\n\n" +
            "Return JSON only, no explanation.";
            String escapedPrompt = prompt.replace("\\", "\\\\")
                             .replace("\"", "\\\"")
                             .replace("\n", "\\n");
            String response = restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"contents\":[{\"parts\":[{\"text\":\"" + escapedPrompt + "\"}]}]}")
                .retrieve()
                .body(String.class);
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);
                String text = root.path("candidates")
                            .get(0)
                            .path("content")
                            .path("parts")
                            .get(0)
                            .path("text")
                            .asText();
                JsonNode result = mapper.readTree(text);
                float score = (float) result.path("score").asDouble();
                List<String> matchedSkills = mapper.convertValue(result.path("matched_skills"), List.class);
                List<String> gaps = mapper.convertValue(result.path("gaps"), List.class);

                matches.add(new JobMatch(job, score, matchedSkills, gaps));
            } catch (Exception e) {
                System.out.println("Failed to score job " + job.getId() + ": " + e.getMessage());
            }
            
        }
        matches.sort((a, b) -> Float.compare(b.getScore(), a.getScore()));
        return matches;
    }
    
}
