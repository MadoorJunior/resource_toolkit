package com.madoor.resource_toolkit.service;

import com.madoor.resource_toolkit.exception.ResourceTypeException;
import com.madoor.resource_toolkit.projection.Concept2ResourcePro;
import com.madoor.resource_toolkit.repository.ConceptRepository;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.util.DocProcessor;
import lombok.RequiredArgsConstructor;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DocumentService {
    private final DocProcessor docProcessor;
    private final ConceptRepository conceptRepository;
    public Response<String> extractText(MultipartFile file) throws ResourceTypeException, IOException {
        return Response.success(docProcessor.extractText(file));
    }
    public Response<List<Keyword>> getKeyWords(MultipartFile file) throws ResourceTypeException, IOException {
        return Response.success(docProcessor.getKeywords(file));
    }
    public Response<Object> computeResourceSimilarity(MultipartFile file) throws ResourceTypeException, IOException {
        List<Keyword> keywords = docProcessor.getKeywords(file);
        List<String> conceptNameList = keywords.stream().map(Keyword::getName).collect(Collectors.toList());
        //获取相关联的概念
        List<Concept2ResourcePro> concept2ResourceList = conceptRepository.findConceptEntitiesByNameIn(conceptNameList);
        //获取关联概念中的资源
        ArrayList<Concept2ResourcePro.Resource> resources = new ArrayList<>();
        for (Concept2ResourcePro concept : concept2ResourceList) {
            if (!concept.getResourceList().isEmpty()){
                for (Concept2ResourcePro.KeywordRel resourceEntity : concept.getResourceList()) {
                    Concept2ResourcePro.Resource resource = resourceEntity.getResource();
                    resources.add(resource);
                }
            }
        }
        //再从每个资源中获取概念和频次
        //与上传的资源的相关概念进行余弦相似度计算
        return Response.success(resources);
    }
}
