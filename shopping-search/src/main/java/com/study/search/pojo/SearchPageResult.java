package com.study.search.pojo;

import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.pojo.Category;

import java.util.List;
import java.util.Map;

public class SearchPageResult extends PageResult<Goods> {

//    存放分类集合
    private List<Category> categories;

//    存放品牌集合
    private List<Brand> brands;

//    存放所有属性
    private List<Map<String,Object>> specs;

    public SearchPageResult(Long total,Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands,List<Map<String,Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
