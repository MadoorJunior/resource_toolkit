package com.madoor.resource_toolkit.controller;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.madoor.resource_toolkit.config.MinioProperties;
import com.madoor.resource_toolkit.entity.ResourceEntity;
import com.madoor.resource_toolkit.projection.Resource2ConceptPro;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.ResourceService;
import com.madoor.resource_toolkit.util.DocProcessor;
import com.madoor.resource_toolkit.util.MinioUtil;
import com.madoor.resource_toolkit.util.WordToPdf;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;

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
    public Response<Object> uploadResource(@RequestParam(name = "file") MultipartFile file,
                                           @RequestParam(name = "section") String section,
                                           @RequestParam(name = "subject") String subject,
                                           @RequestParam(name = "type") String type) throws Exception {
        return resourceService.upload(file,section,subject,type);
    }
    @PostMapping("/convert")
    public void importPdfFile(@RequestParam("file") MultipartFile file) throws Exception {
        docProcessor.uploadSlidePreview(file,System.currentTimeMillis()+".pdf",12323);
    }
}
