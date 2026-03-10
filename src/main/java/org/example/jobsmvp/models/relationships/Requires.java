package org.example.jobsmvp.models.relationships;

import org.example.jobsmvp.models.nodes.Technology;
import org.springframework.data.neo4j.core.schema.*;
import lombok.Data;

@RelationshipProperties
@Data
public class Requires {
    @Id @GeneratedValue
    private String id;

    private String importance;

    @Property("min_proficiency")
    private Integer minProficiency;

    @TargetNode
    private Technology technology;
}