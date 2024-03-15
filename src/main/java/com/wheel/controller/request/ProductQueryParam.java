package com.wheel.controller.request;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductQueryParam  implements Serializable {

    /**
     * in_basket，category_id，all
     */
    private String queryKey;

    /**
     * in_basket，category_id
     */
    private String queryValue;

    private Integer pageIndex;

    private Integer pageSize;

}
