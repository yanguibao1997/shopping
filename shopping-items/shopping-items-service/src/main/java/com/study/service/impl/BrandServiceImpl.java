package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.mapper.BrandMapper;
import com.study.mapper.CategoryBrandMapper;
import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.pojo.CategoryBrand;
import com.study.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer pageNo, Integer pageSize, Boolean isDes, String sortBy) {

        if(pageSize!=-1){
            PageHelper.startPage(pageNo,pageSize);
        }

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

    @Override
    @Transactional
    public void addBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        cids.forEach( cid -> {
//            添加数据到  关联表
            brandMapper.insertCategoryBrand(cid,brand.getId());
        });
    }

    @Override
    public void deleteCategoryBrandByBid(Long bid) {
        CategoryBrand categoryBrand=new CategoryBrand();
        categoryBrand.setBrandId(bid);
        categoryBrandMapper.delete(categoryBrand);
    }

    @Override
    public void updateBrandByid(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void deleteBrandById(Long bid) {
        brandMapper.deleteByPrimaryKey(bid);
    }

    @Override
    public Brand queryBrandByBid(Long bid) {
        Brand brand = this.brandMapper.selectByPrimaryKey(bid);
        return brand;
    }

    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brands = brandMapper.queryBrandByCid(cid);
        return brands;
    }

    @Override
    public Brand queryBrandById(Long bid) {
        Brand brand = brandMapper.selectByPrimaryKey(bid);
        return brand;
    }
}
