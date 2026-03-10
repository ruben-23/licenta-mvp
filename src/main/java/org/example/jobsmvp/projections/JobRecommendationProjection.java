package org.example.jobsmvp.projections;

import java.util.List;

public record JobRecommendationProjection(
        String jobId,
        String title,
        String companyName,
        String location,
        Boolean remote,
        Double similarityScore,
        List<String> matchedTechnologies,
        List<String> missingTechnologies
) {}