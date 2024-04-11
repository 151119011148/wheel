package com.wheel.dao.dataObject;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@Table(name = "customer_show")
@EntityListeners(AuditingEntityListener.class)
public class CustomerShowDO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "show_id")
    private String customerShowId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "primary_image")
    private Integer primaryImage;

    @Column(name = "image")
    private String image;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "car_model")
    private String carModel;

    @CreatedDate
    @Column(name = "gmt_create", nullable = false, updatable = false)
    @Temporal(TIMESTAMP)
    private Date createdTime;

    @LastModifiedDate
    @Column(name = "gmt_modified", nullable = false)
    @Temporal(TIMESTAMP)
    private Date modifiedTime;

    @Column(name = "is_removed")
    private Integer isRemoved;

    public void update(CustomerShowDO update) {
        if (StringUtils.isNotBlank(update.productId)) {
            this.productId = update.productId;
        }
        if (StringUtils.isNotBlank(update.productName)) {
            this.productName = update.productName;
        }
        if (StringUtils.isNotBlank(update.categoryId)) {
            this.categoryId = update.categoryId;
        }
        if (StringUtils.isNotBlank(update.categoryName)) {
            this.categoryName = update.categoryName;
        }
        if (Objects.nonNull(update.primaryImage)) {
            this.primaryImage = update.primaryImage;
        }
        if (StringUtils.isNotBlank(update.image)) {
            this.image = update.image;
        }

        if (StringUtils.isNotBlank(update.brandName)) {
            this.brandName = update.brandName;
        }
        if (StringUtils.isNotBlank(update.carModel)) {
            this.carModel = update.carModel;
        }

    }
}
