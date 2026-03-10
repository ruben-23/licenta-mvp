package org.example.jobsmvp.config;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MemgraphConfig {

    @Bean
    public Configuration cypherDslConfiguration() {
        // This forces Spring Data Neo4j to use the older dialect,
        // replacing 'elementId()' with the standard 'id()' function that Memgraph expects.
        return Configuration.newConfig()
                .withDialect(Dialect.NEO4J_4)
                .build();
    }
}