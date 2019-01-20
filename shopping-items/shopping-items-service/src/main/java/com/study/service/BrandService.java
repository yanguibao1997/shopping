package com.study.service;


import com.study.page.PageResult;
import com.study.pojo.Brand;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {

    public PageResult<Brand> queryBrandByPage(String key,Integer pageNo,Integer pageSize,Boolean isDes,String sortBy);

    public void addBrand(Brand brand, List<Long> cid);

    public void deleteCategoryBrandByBid(Long bid);

    public void updateBrandByid(Brand brand);

    public void deleteBrandById(Long bid);
}
