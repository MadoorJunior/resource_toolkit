package com.madoor.resource_toolkit.util.aspose;

import java.io.InputStream;

public interface IFileConvert {

    /**
     * pdf文件后缀
     */
    String FILE_PDF_SUFFIX = ".pdf";

    default String getResultPath(String filePath, String fileSuffix){
        // 什么将要返回的pdf路径
        int i = filePath.lastIndexOf(".");
        String path = filePath.substring(0, i);
        return path + fileSuffix;
    }

    String fileToPdf(InputStream docPathInputStream) throws Exception;
}