package com.madoor.resource_toolkit.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConceptVO {
    String name;
    Integer frequency;
}
