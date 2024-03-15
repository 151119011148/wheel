package com.wheel.controller.response;

import com.wheel.util.TreeNode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CategoryVO implements TreeNode<CategoryVO, String>, Serializable {

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

    private List<CategoryVO> children;

    private Date createdTime;

    private Date modifiedTime;

    @Override
    public String id() {
        return categoryId;
    }

    @Override
    public String parentId() {
        return parentId;
    }

    @Override
    public List<CategoryVO> children() {
        return children;
    }

    @Override
    public void children(List<CategoryVO> children) {
        this.children = children;
    }
}
