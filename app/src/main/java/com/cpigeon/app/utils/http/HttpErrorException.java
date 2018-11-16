package com.cpigeon.app.utils.http;

import com.cpigeon.app.utils.databean.ApiResponse;

public class HttpErrorException extends RuntimeException {
    private ApiResponse ApiResponse;
    public HttpErrorException(ApiResponse ApiResponse) {
        super(ApiResponse!=null?ApiResponse.msg:"");
        this.ApiResponse=ApiResponse;
    }

    public ApiResponse getResponseJson() {
        return ApiResponse;
    }
}