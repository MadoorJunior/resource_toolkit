package com.madoor.resource_toolkit.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResourceVO {
    Long id;
    List<ConceptVO> conceptList;
}
