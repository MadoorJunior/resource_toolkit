package com.madoor.resource_toolkit.controller;

import com.madoor.resource_toolkit.dto.CreateDirectoryDto;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resource/toolkit/file")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public Response<String> upload(@RequestParam(name = "file") MultipartFile file,
                                   @RequestParam(name = "key") String key,
                                   @RequestParam(name = "path") String path) throws Exception {
        return fileService.uploadFile(file,key,path);
    }
    @GetMapping("/info/{file_name}")
    public Response<String> info(@PathVariable(name = "file_name") String fileName) throws Exception {
        return fileService.fileInfo(fileName);
    }
    @PostMapping("/directory")
    public Response<String> createDir(@RequestBody CreateDirectoryDto createDirectoryDto) throws Exception {
        return fileService.createDirectory(createDirectoryDto);
    }

}
