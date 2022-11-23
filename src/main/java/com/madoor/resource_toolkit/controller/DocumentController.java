package com.madoor.resource_toolkit.controller;

import com.madoor.resource_toolkit.entity.ConceptEntity;
import com.madoor.resource_toolkit.entity.ResourceEntity;
import com.madoor.resource_toolkit.exception.ResourceTypeException;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.ConceptService;
import com.madoor.resource_toolkit.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/resource/toolkit/document")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DocumentController {
    private final DocumentService documentService;
    @PostMapping("/text")
    public Response<String> extractText(@RequestParam MultipartFile file) throws Exception {
        return documentService.extractText(file);
    }
    @PostMapping("/keyword")
    public Response<List<Keyword>> getKeywords(@RequestParam MultipartFile file) throws ResourceTypeException, IOException {
        return documentService.getKeyWords(file);
    }
    @PostMapping("/similarity")
    public Response<Object> computeSimilarity(@RequestParam MultipartFile file) throws ResourceTypeException, IOException {
        return documentService.computeResourceSimilarity(file);
    }
}
