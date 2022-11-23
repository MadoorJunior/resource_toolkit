package com.madoor.resource_toolkit.projection;

import java.util.List;

public interface Resource2ConceptPro {
    Long getId();
    String getName();
    Integer getSubjectId();
    List<KeywordRel> getConceptList();
    interface KeywordRel{
        Double getWeight();
        Integer getFrequency();
        Concept getConcept();
    }
    interface Concept{
        String getName();
    }
}
