package com.madoor.resource_toolkit.service;

import com.madoor.resource_toolkit.config.MinioProperties;
import com.madoor.resource_toolkit.dto.ResourceFileInfo;
import com.madoor.resource_toolkit.entity.ConceptEntity;
import com.madoor.resource_toolkit.entity.ResourceEntity;
import com.madoor.resource_toolkit.mapper.ResourceMapper;
import com.madoor.resource_toolkit.mapper.SectionMapper;
import com.madoor.resource_toolkit.mapper.SubjectMapper;
import com.madoor.resource_toolkit.pojo.Resource;
import com.madoor.resource_toolkit.projection.ConceptPro;
import com.madoor.resource_toolkit.projection.Resource2ConceptPro;
import com.madoor.resource_toolkit.relationship.Resource2Concept;
import com.madoor.resource_toolkit.repository.ConceptRepository;
import com.madoor.resource_toolkit.repository.ResourceRepository;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.util.DocProcessor;
import com.madoor.resource_toolkit.util.MinioUtil;
import com.madoor.resource_toolkit.util.Video2Img;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.ansj.app.keyword.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final SectionMapper sectionMapper;
    private final SubjectMapper subjectMapper;
    private final ResourceMapper resourceMapper;
    private final MinioProperties minioProperties;
    private final DocProcessor docProcessor;
    private final ConceptRepository conceptRepository;
    private final Video2Img video2Img;
    public Response<Resource2ConceptPro> getResourceById(Long id){
        Resource2ConceptPro resource = resourceRepository.findResourceEntityById(id);
        if (resource!=null){
            return Response.success(resource);
        }
        return Response.fail("未找到相关资源");
    }
    public Response<?> upload(MultipartFile file, String section, String subject, String type, String desc) throws Exception {
        //校验学段，学科，资源类型是否符合规范
        Integer sectionId = sectionMapper.getIdByName(section);
        Integer subjectId = subjectMapper.getIdByName(subject);
        Integer resourceType = resourceMapper.getTypeByName(type);
        if(sectionId==null||subjectId==null||resourceType==null){
            return Response.fail("请检查资源信息是否符合规范");
        }
        //构造文件夹路径
        String path = sectionId + "/" + subjectId + "/" + resourceType + "/";
        //预览路径
        String previewPath = "preview/" + path;
        //截取文件名与文件类型
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int dot = originalFilename.lastIndexOf(".");
        String resourceName = originalFilename.substring(0,dot);
        String suffix = originalFilename.substring(dot+1);
        List<Keyword> keywords;
        //提取关键词
        if (!suffix.equals("mp4")){
            keywords = docProcessor.getKeywords(file);
        }else {
            keywords = docProcessor.getKeywords(file, desc);
        }
        //构造资源信息
        Resource resource = Resource.builder().resourceName(resourceName)
                .browse(0).download(0).updateTime(LocalDate.now()).resourceType(resourceType)
                .period(sectionId).subject(subjectId).fileType(suffix).isFeatured(0).build();
        //资源信息入库MySql
        resourceMapper.insert(resource);
        //生成封面

        if (suffix.equals("pptx")||suffix.equals("ppt")){
            docProcessor.uploadSlidePreview(file,previewPath + resource.getId() + ".pdf", resource.getId());
        } else if (suffix.equals("doc") || suffix.equals("docx")) {
            docProcessor.uploadWordPreview(file,previewPath + resource.getId() + ".pdf", resource.getId());
        } else if (suffix.equals("pdf")) {
            docProcessor.uploadThumbImg(file.getInputStream(), resource.getId());
        }else if (suffix.equals("mp4")){
            video2Img.getVideoPic(file, resource.getId());
        }

        //上传Minio
        MinioUtil.uploadFile(minioProperties.getBucket(), file, originalFilename, path + resource.getId() + "." + suffix);
        //生成预览pdf
        //计算资源相似度
//        similarityUtil.calculate(keywords);
        //插入neo4j
        ResourceEntity resourceEntity = new ResourceEntity(resource.getId().longValue(), resourceName, subjectId);
        //构造关系边
        ArrayList<Resource2Concept> resource2ConceptList = new ArrayList<>();
        for (Keyword keyword : keywords) {
            //查找概念节点是否存在，存在则生成keyword关系
            ConceptPro conceptNode = conceptRepository.findFirstByName(keyword.getName());
            if (conceptNode!=null){
                Resource2Concept resource2Concept = new Resource2Concept(keyword.getFreq(), keyword.getScore(), new ConceptEntity(conceptNode.getId(), conceptNode.getName()));
                resource2ConceptList.add(resource2Concept);
            }
        }
        resourceEntity.setConceptList(resource2ConceptList);
        //写入neo4j
        resourceRepository.save(resourceEntity);
        //生成缩略图
        return Response.success("资源上传成功");
    }
}
