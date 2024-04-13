package com.wheel.controller.response;

import com.google.common.collect.Lists;
import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.dao.dataObject.ProductDO;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class CustomerShowVO {

    private String productId;

    private String productName;

    private String categoryId;

    private String categoryName;

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

        private String carModel;

    }






    public static CustomerShowVO read4(CustomerShowDO param) {
        CustomerShowVO result = new CustomerShowVO();
        result.setProductId(param.getProductId());
        result.setProductName(param.getProductName());
        result.setCategoryId(param.getCategoryId());
        result.setCategoryName(param.getCategoryName());
        result.setCreatedTime(param.getCreatedTime());
        result.setModifiedTime(param.getModifiedTime());
        ShowInfo showInfo = new ShowInfo();
        showInfo.setShowId(param.getCustomerShowId());
        showInfo.setPrimaryImage(param.getPrimaryImage());
        showInfo.setImage(param.getImage());
        showInfo.setBrandName(param.getBrandName());
        showInfo.setCarModel(param.getCarModel());
        result.setShowInfo(Lists.newArrayList(showInfo));
        return result;
    }

    public static CustomerShowDO convert2(CustomerShowVO param) {
        CustomerShowDO result = new CustomerShowDO();
        result.setProductId(param.getProductId());
        result.setProductName(param.getProductName());
        result.setCategoryId(param.getCategoryId());
        result.setCategoryName(param.getCategoryName());
        if (CollectionUtils.isNotEmpty(param.getShowInfo())){
            result.setPrimaryImage(param.getShowInfo().get(0).getPrimaryImage());
            result.setImage(param.getShowInfo().get(0).getImage());
            result.setBrandName(param.getShowInfo().get(0).getBrandName());
            result.setCarModel(param.getShowInfo().get(0).getCarModel());
        }
        result.setCreatedTime(param.getCreatedTime());
        result.setModifiedTime(param.getModifiedTime());
        result.setIsRemoved(0);
        return result;
    }

}
