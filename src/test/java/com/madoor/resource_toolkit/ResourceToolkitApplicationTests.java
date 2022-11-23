package com.madoor.resource_toolkit;

import com.madoor.resource_toolkit.entity.ResourceEntity;
import com.madoor.resource_toolkit.repository.ResourceRepository;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ResourceToolkitApplicationTests {
    @Autowired
    private ResourceRepository resourceRepository;
    @Test
    void contextLoads() {
        Optional<ResourceEntity> byId = resourceRepository.findById(791033L);
        System.out.println(byId.get());
    }

}
