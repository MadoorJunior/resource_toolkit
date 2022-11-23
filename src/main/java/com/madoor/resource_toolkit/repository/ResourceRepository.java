package com.madoor.resource_toolkit.repository;

import com.madoor.resource_toolkit.entity.ConceptEntity;
import com.madoor.resource_toolkit.entity.ResourceEntity;
import com.madoor.resource_toolkit.projection.ConceptPro;
import com.madoor.resource_toolkit.projection.Resource2ConceptPro;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends CrudRepository<ResourceEntity,Long> {
    Resource2ConceptPro findResourceEntityById(Long id);
    Resource2ConceptPro findFirstById(Long id);
    ConceptPro findFirstByName(String name);
}
