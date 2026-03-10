package org.example.jobsmvp.repositories;

import org.example.jobsmvp.models.nodes.Student;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface StudentRepository extends Neo4jRepository<Student, String> {}
