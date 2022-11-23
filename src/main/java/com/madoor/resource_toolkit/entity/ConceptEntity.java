package com.madoor.resource_toolkit.entity;

import com.madoor.resource_toolkit.relationship.Concept2Resource;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
@Data
@Node("concept")
public class ConceptEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "keyword",direction = Relationship.Direction.INCOMING)
    private List<Concept2Resource> resourceList;


    public ConceptEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
