package org.example.jobsmvp.models.relationships;

import org.example.jobsmvp.models.nodes.Job;
import org.springframework.data.neo4j.core.schema.*;
import lombok.Data;



@RelationshipProperties
@Data
public class Posts {
    @Id @GeneratedValue
    private String id;

    @Property("is_active")
    private Boolean isActive;

    @TargetNode
    private Job job;
}
