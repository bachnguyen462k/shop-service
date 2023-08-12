package com.example.demo.exception;

import com.example.demo.model.base.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeoutRestTemplateException extends RuntimeException {
    private BaseResponse response;
    private String language;
    private String path = "";

    public TimeoutRestTemplateException( String path) {
        this.path = path;
    }
}
