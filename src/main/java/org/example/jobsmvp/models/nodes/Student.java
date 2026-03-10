package org.example.jobsmvp.models.nodes;

import lombok.Data;
import org.example.jobsmvp.models.relationships.Knows;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.List;

@Node("Student")
@Data
public class Student {
    @Id
    private String student_id;

    private String name;
    private String major;

    @Property("graduation_year")
    private Integer graduationYear;

    @Property("current_year_of_study")
    private Integer currentYearOfStudy;

    @Property("degree_level")
    private String degreeLevel;

    // Relationships with properties
    @Relationship(type = "KNOWS", direction = Relationship.Direction.OUTGOING)
    private List<Knows> knownTechnologies;

    // Relationships without properties
    @Relationship(type = "CREATED", direction = Relationship.Direction.OUTGOING)
    private List<Project> projects;

    @Relationship(type = "COMPLETED", direction = Relationship.Direction.OUTGOING)
    private List<Course> courses;

    @Relationship(type = "EARNED", direction = Relationship.Direction.OUTGOING)
    private List<Diploma> diplomas;
}