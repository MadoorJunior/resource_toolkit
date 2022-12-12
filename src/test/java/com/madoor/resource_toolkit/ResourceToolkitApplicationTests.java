package com.madoor.resource_toolkit;

import com.madoor.resource_toolkit.repository.ResourceRepository;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
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
        try {
            //这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("G:\\package\\aspose-slides-21.10-jdk16.jar");
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.slides.internal.of.public");
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods();
            for (CtMethod ctMethod : methodA) {
                CtClass[] ps = ctMethod.getParameterTypes();
                if (ps.length == 3 && ctMethod.getName().equals("do")) {
                    System.out.println("ps[0].getName==" + ps[0].getName());
                    ctMethod.setBody("{}");
                }
            }
            //这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("G:\\package");
        } catch (Exception e) {
            System.out.println("错误==" + e);
        }

    }

}
