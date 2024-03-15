package com.wheel.controller.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryParam implements Serializable {

    /**
     *
     */
    private String parentId;

    /**
     *
     */
    private Integer level;

    /**
     *
     */
    private String categoryId;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String categoryDesc;

}
