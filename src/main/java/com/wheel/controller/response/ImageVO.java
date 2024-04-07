package com.wheel.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class ImageVO {

    private String fileName;

    private String url;

    private Boolean isUsed;


    public ImageVO(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
