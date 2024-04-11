package com.wheel.controller.response;

import lombok.Data;

import java.util.Date;

@Data
public class ImageVO {

    private String fileName;

    private String url;

    private Boolean isUsed;

    private Date modifiedTime;


    public ImageVO(String fileName, String url, Date createdTime) {
        this.fileName = fileName;
        this.url = url;
        this.modifiedTime = createdTime;
    }
}
