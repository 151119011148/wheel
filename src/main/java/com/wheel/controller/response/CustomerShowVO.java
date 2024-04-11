package com.wheel.controller.response;

import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.dao.dataObject.ProductDO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerShowVO {

    private String productId;

    private String categoryId;

    private List<ShowInfo> showInfo;

    private Date createdTime;

    private Date modifiedTime;


    @Data
    public static class ShowInfo {

        private String showId;

        /**
         * 0 非主图， 1 主图
         */
        private Integer primaryImage;

        private String image;

        /**
         * 品牌名称
         */
        private String brandName;

    }






    public static CustomerShowVO read4(CustomerShowDO param) {
        CustomerShowVO result = new CustomerShowVO();
        result.setProductId(param.getProductId());
        result.setCreatedTime(param.getCreatedTime());
        result.setModifiedTime(param.getModifiedTime());
        return result;
    }

    public static CustomerShowDO convert2(CustomerShowVO param) {
        CustomerShowDO result = new CustomerShowDO();
        result.setProductId(param.getProductId());

        result.setCreatedTime(param.getCreatedTime());
        result.setModifiedTime(param.getModifiedTime());
        result.setIsRemoved(0);
        return result;
    }

}
