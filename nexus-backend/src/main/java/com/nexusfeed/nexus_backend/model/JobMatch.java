package com.nexusfeed.nexus_backend.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobMatch {
    private Job job;
    private float score;
    private List<String> matchedSkills;
    private List<String> gaps;

}
