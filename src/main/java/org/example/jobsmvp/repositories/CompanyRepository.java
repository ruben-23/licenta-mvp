package org.example.jobsmvp.repositories;
import org.example.jobsmvp.models.nodes.Company;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

public interface CompanyRepository extends Neo4jRepository<Company, String> {
    @Query("MATCH (c:Company) RETURN c SKIP $skip LIMIT $limit")
    List<Company> findAllPaginated(int skip, int limit);
}