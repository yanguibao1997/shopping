package com.study.service;


import com.study.page.PageResult;
import com.study.pojo.Brand;

public interface BrandService {

    public PageResult<Brand> queryBrandByPage(String key,Integer pageNo,Integer pageSize,Boolean isDes,String sortBy);
}
