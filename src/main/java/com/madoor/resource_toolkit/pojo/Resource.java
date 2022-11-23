package com.madoor.resource_toolkit.pojo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Resource {
    Integer id;
    String resourceName;
    Integer download;
    Integer browse;
    LocalDate updateTime;
    Integer resourceType;
    Integer period;
    Integer subject;
    String fileType;
    Integer isFeatured;
}
