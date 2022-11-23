package com.madoor.resource_toolkit.entity;

import com.madoor.resource_toolkit.relationship.Resource2Concept;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Data
@Node("resource")
public class ResourceEntity {
    @Id
    private Long id;
    private String name;
    @Property("学科")
    private Integer subjectId;
    @Relationship(type = "keyword",direction = Relationship.Direction.OUTGOING)
    private List<Resource2Concept> conceptList;

    public ResourceEntity(Long id, String name, Integer subjectId) {
        this.id = id;
        this.name = name;
        this.subjectId = subjectId;
    }
}
