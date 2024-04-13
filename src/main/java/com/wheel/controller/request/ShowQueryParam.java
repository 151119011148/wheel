package com.wheel.controller.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowQueryParam implements Serializable {


    /**
     * in_basket，category_id
     */
    private String queryValue;

    private Integer pageIndex;

    private Integer pageSize;

}
