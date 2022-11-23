package com.madoor.resource_toolkit.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200,"响应成功"),
    FAILED(400,"响应失败"),
    UNSUPPORTED_RESOURCE_TYPE(415,"格式错误，请上传docx、pptx、pdf格式文件");

    public int code;
    public String message;
}
