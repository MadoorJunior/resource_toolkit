package com.madoor.resource_toolkit.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.*;


public class WordToPdf {
    public static File docToPdf(String docPath, String pdfPath) {
        File pdfFile = new File(pdfPath);
        try {
            long old = System.currentTimeMillis();
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License license = new License();
            license.setLicense(is);
            Document document = new Document(docPath);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            document.save(outputStream, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            outputStream.close();
            is.close();
            System.out.println("WORD转化PDF共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            System.out.println("转化失败");
            e.printStackTrace();
        }
        return pdfFile;
    }

    public static InputStream docToPdf(InputStream docPathInputStream) {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            long old = System.currentTimeMillis();
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License license = new License();
            license.setLicense(is);
            Document document = new Document(docPathInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream, SaveFormat.PDF);
            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            long now = System.currentTimeMillis();
            is.close();
            System.out.println("WORD转化PDF共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            System.out.println("转化失败");
            e.printStackTrace();
        }
        return byteArrayInputStream;
    }
}

