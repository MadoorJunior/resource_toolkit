package com.madoor.resource_toolkit.controller;

import com.madoor.resource_toolkit.config.MinioProperties;
import com.madoor.resource_toolkit.projection.Resource2ConceptPro;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.ResourceService;
import com.madoor.resource_toolkit.util.DocProcessor;
import com.madoor.resource_toolkit.util.Video2Img;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/resource/toolkit/document/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceController {
    private final ResourceService resourceService;
    private final MinioProperties minioProperties;
    private final DocProcessor docProcessor;
    @GetMapping("/{id}")
    public Response<Resource2ConceptPro> getResource(@PathVariable("id") Long id){
        return resourceService.getResourceById(id);
    }
    @PostMapping("/upload")
    public Response<?> uploadResource(@RequestParam(name = "file") MultipartFile file,
                                      @RequestParam(name = "section") String section,
                                      @RequestParam(name = "subject") String subject,
                                      @RequestParam(name = "type") String type,
                                      @RequestParam(name = "desc",required = false) String desc) throws Exception {
        return resourceService.upload(file,section,subject,type,desc);
    }
    @PostMapping("/convert")
    public void importPdfFile(@RequestParam("file") MultipartFile file) throws Exception {
        docProcessor.uploadSlidePreview(file,System.currentTimeMillis()+".pdf",12323);
    }
}
