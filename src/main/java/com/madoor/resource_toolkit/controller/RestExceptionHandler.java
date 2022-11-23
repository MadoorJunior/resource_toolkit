package com.madoor.resource_toolkit.controller;

import com.madoor.resource_toolkit.exception.ResourceTypeException;
import com.madoor.resource_toolkit.response.Response;
import com.madoor.resource_toolkit.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ResourceTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response resourceTypeError(Exception e) {
        return Response.fail(ResultCode.UNSUPPORTED_RESOURCE_TYPE);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return Response.fail(e.getMessage());
    }

}