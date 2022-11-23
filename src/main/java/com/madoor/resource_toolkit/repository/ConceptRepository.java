package com.madoor.resource_toolkit.repository;

import com.madoor.resource_toolkit.entity.ConceptEntity;
import com.madoor.resource_toolkit.projection.Concept2ResourcePro;
import com.madoor.resource_toolkit.projection.ConceptPro;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface ConceptRepository extends CrudRepository<ConceptEntity,Long> {
    Concept2ResourcePro findConceptEntityByName(String name);
    List<Concept2ResourcePro> findConceptEntitiesByNameIn(List<String> keywords);
    ConceptPro findFirstByName(String name);
}
