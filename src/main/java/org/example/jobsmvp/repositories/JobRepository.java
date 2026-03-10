package org.example.jobsmvp.repositories;

import org.example.jobsmvp.models.nodes.Job;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

public interface JobRepository extends Neo4jRepository<Job, String> {
    @Query("MATCH (c:Company {company_id: $companyId})-[:POSTS]->(j:Job) " +
            "OPTIONAL MATCH (j)-[r:REQUIRES]->(t:Technology) " +
            "RETURN j, collect(r), collect(t)")
    List<Job> findJobsByCompany(String companyId);
}