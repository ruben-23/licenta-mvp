package org.example.jobsmvp.models.nodes;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Technology")
@Data
public class Technology {
    @Id
    private String tech_id;

    private String name;
    private String category;
}