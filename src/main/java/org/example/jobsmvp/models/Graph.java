package org.example.jobsmvp.models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Graph {

    @Id
    @GeneratedValue
    private Long id;
}