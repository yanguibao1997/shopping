package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.mapper.BrandMapper;
import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer pageNo, Integer pageSize, Boolean isDes, String sortBy) {

        PageHelper.startPage(pageNo,pageSize);
//        排序
        Example example=new Example(Brand.class);
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy + (isDes?" desc":" asc"));
        }
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        List<Brand> brands = brandMapper.selectByExample(example);

        PageInfo<Brand> pageInfo=new PageInfo<>(brands);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }
}
