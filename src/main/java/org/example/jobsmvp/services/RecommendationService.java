package org.example.jobsmvp.services;

import org.example.jobsmvp.models.nodes.Job;
import org.example.jobsmvp.models.nodes.Student;
import org.example.jobsmvp.projections.JobRecommendationProjection;
import org.example.jobsmvp.projections.StudentRecommendationProjection;
import org.example.jobsmvp.repositories.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class RecommendationService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final RecommendationRepository recommendationRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public RecommendationService(StudentRepository studentRepository, JobRepository jobRepository, RecommendationRepository recommendationRepository) {
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
        this.recommendationRepository = recommendationRepository;
        new File("data/json").mkdirs();
    }

    public Map<String, String> getRecommendedStudentsForJob(String jobId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow();
        List<Student> allStudents = studentRepository.findAll();

        Map<String, Object> context = new HashMap<>();
        context.put("job", job);
        context.put("students", allStudents);

        String jsonContext = objectMapper.writeValueAsString(context);
        Files.writeString(Paths.get("data/json/job_" + jobId + "_context.json"), jsonContext);

        // UPDATED PROMPT: Ask for Markdown
        String prompt = "You are an expert tech recruiter. Analyze the following JSON data containing a job description and a pool of candidates. " +
                "Write a detailed recommendation report in Markdown format. Use headings (##), bold text for names, bullet points, and highlight the match percentage for the top candidates. " +
                "Explain your reasoning clearly.\n\n" + jsonContext;

        return callGemini(prompt);
    }

    public Map<String, String> getRecommendedJobsForStudent(String studentId) throws Exception {
        Student student = studentRepository.findById(studentId).orElseThrow();
        List<Job> allJobs = jobRepository.findAll();

        System.out.println("Getting recommended jobs for student: " + studentId);

        Map<String, Object> context = new HashMap<>();
        context.put("student", student);
        context.put("jobs", allJobs);

        String jsonContext = objectMapper.writeValueAsString(context);
        Files.writeString(Paths.get("data/json/student_" + studentId + "_context.json"), jsonContext);

        // UPDATED PROMPT: Ask for Markdown
        String prompt = "You are an expert career advisor. Analyze the following JSON data containing a student's profile and a list of available jobs. " +
                "Write a detailed recommendation report in Markdown format. Use headings (##), bold text for job titles, bullet points, and highlight the match percentage for the top recommended jobs. " +
                "Explain why these roles fit the student's skills.\n\n" + jsonContext;

        return callGemini(prompt);
    }

    private Map<String, String> callGemini(String prompt) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;

        System.out.println("Calling Gemini API...");

        // Simplified payload: No generationConfig needed for Markdown
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> requestBodyMap = Map.of("contents", List.of(parts));

        String payload = objectMapper.writeValueAsString(requestBodyMap);

        System.out.println("Built payload:" + payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Gemini API Response: " + response.body() + "\n");

        JsonNode rootNode = objectMapper.readTree(response.body());

        if (rootNode.has("error")) {
            throw new RuntimeException("Gemini API Error: " + rootNode.path("error").path("message").asText());
        }
        System.out.println("Extracting markdown from Gemini response...");
        // Extract the raw Markdown text
        String markdownText = rootNode.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

        // Wrap the Markdown string safely in a JSON object to send to the frontend
        Map<String, String> responseData = new HashMap<>();
        responseData.put("markdown", markdownText);
        System.out.println(responseData);

        return responseData;
    }

    // 1. Student looking for jobs
    public List<JobRecommendationProjection> getJobMatchesByEmbedding(String studentId, int limit) {
        return recommendationRepository.recommendJobsForStudent(studentId, limit);
    }

    // 2. Recruiter looking for students
    public List<StudentRecommendationProjection> getStudentMatchesByEmbedding(String jobId, int limit) {
        return recommendationRepository.recommendStudentsForJob(jobId, limit);
    }



}
