package org.example.jobsmvp.projections;

import java.util.List;

/**
 * Projection record for returning recommended students for a specific job.
 */
public record StudentRecommendationProjection(
        String studentId,
        String name,
        String major,
        String degreeLevel,
        Integer graduationYear,
        Double similarityScore,
        List<String> matchedTechnologies,
        List<String> missingTechnologies
) {}