package org.example.jobsmvp;

import org.example.jobsmvp.repositories.GraphRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JobsMvpApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsMvpApplication.class, args);
	}

	@Bean
	CommandLineRunner runEmbeddings(GraphRepository graphRepository) {
		return args -> {

			// 1. Ensure Vector Indices Exist
			try {
				graphRepository.createStudentVectorIndex();
				System.out.println("Created 'student_embeddings' vector index.");
			} catch (Exception e) {}

			try {
				graphRepository.createJobVectorIndex();
				System.out.println("Created 'job_embeddings' vector index.");
			} catch (Exception e) {}

			// 2. Generate the Embeddings
			Long nodesProcessed = graphRepository.generateNode2VecEmbeddings();
			System.out.println("Embeddings generated");
		};
	}

}
