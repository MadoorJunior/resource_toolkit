package com.madoor.resource_toolkit.relationship;

import com.madoor.resource_toolkit.entity.ResourceEntity;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class Concept2Resource {
    @RelationshipId
    private Long id;
    @Property("num")
    private final Integer frequency;
    private final Double weight;
    @TargetNode
    private final ResourceEntity resource;
}
