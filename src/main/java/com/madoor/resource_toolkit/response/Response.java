package com.madoor.resource_toolkit.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response<T> {
    Integer code;
    String message;
    T data;
    public Response(ResultCode resultCode, T data) {
        this.code = resultCode.code;
        this.message = resultCode.message;
        this.data = data;
    }
    public Response(ResultCode resultCode) {
        this.code = resultCode.code;
        this.message = resultCode.message;
    }
    public static <T> Response<T> success(T data){
        return new Response<>(ResultCode.SUCCESS,data);
    }
    public static Response fail(){
        return new Response(ResultCode.FAILED);
    }
    public static Response fail(ResultCode resultCode){
        return new Response(resultCode);
    }
    public static Response fail(String errorMessage){
        return new Response(ResultCode.FAILED).setMessage(errorMessage);
    }
}
