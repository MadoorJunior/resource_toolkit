package com.madoor.resource_toolkit.mapper;

import com.madoor.resource_toolkit.pojo.Resource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper {
    public Integer getTypeByName(String name);
    public Integer insert(Resource resource);
}
