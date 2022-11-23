package com.madoor.resource_toolkit.controller;

import com.madoor.resource_toolkit.projection.Concept2ResourcePro;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.ConceptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource/toolkit/document")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConceptController {
    private final ConceptService conceptService;
    @GetMapping("/concept/{name}")
    public Response<Concept2ResourcePro> getConceptByName(@PathVariable("name") String name){
        return conceptService.getConceptByName(name);
    }
}
