package com.madoor.resource_toolkit.projection;

import java.util.List;

public interface Concept2ResourcePro {
    Long getId();
    String getName();
    List<KeywordRel> getResourceList();
    interface KeywordRel{
        Double getWeight();
        Integer getFrequency();
        Resource getResource();
    }
    interface Resource{
        Long getId();
        String getName();
        Integer getSubjectId();
    }
}
