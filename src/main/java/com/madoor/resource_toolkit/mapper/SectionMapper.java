package com.madoor.resource_toolkit.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SectionMapper {
    public Integer getIdByName(String name);
}
