package com.madoor.resource_toolkit.service;

import com.madoor.resource_toolkit.config.MinioProperties;
import com.madoor.resource_toolkit.dto.CreateDirectoryDto;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.util.MinioUtil;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FileService {
    private final MinioProperties minioProperties;
    public Response<String> uploadFile(MultipartFile file,String key,String path) throws Exception {
        MinioUtil.createBucket(minioProperties.getBucket());
        String originalFilename = file.getOriginalFilename();
        //提取后缀名
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        ObjectWriteResponse objectWriteResponse = MinioUtil.uploadFile(minioProperties.getBucket(), file, originalFilename, path + key + suffix);
        return Response.success("文件上传成功");
    }
    public Response<String> fileInfo(String fileName) throws Exception {
        String url = MinioUtil.getPreSignedObjectUrl(minioProperties.getBucket(), fileName);
        return Response.success(url);
    }

    public Response<String> createDirectory(CreateDirectoryDto createDirectoryDto) throws Exception {
        MinioUtil.createDir(createDirectoryDto.getBucket(), createDirectoryDto.getPath());
        return Response.success("创建文件路径成功");
    }
}
