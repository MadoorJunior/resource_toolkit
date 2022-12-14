package com.madoor.resource_toolkit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.madoor.resource_toolkit.mapper")
public class ResourceToolkitApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceToolkitApplication.class, args);
    }

}
