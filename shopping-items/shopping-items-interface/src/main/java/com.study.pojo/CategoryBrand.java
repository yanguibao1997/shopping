package com.study.pojo;

import javax.persistence.Table;

@Table(name = "tb_category_brand")
public class CategoryBrand {

    private Long categoryId;
    private Long brandId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
