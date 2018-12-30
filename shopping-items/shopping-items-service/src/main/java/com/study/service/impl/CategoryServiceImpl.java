package com.study.service.impl;

import com.study.mapper.CategoryMapper;
import com.study.pojo.Category;
import com.study.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> queryByParentId(Long parentId) {
        Category category=new Category();
        category.setParentId(parentId);
        return categoryMapper.select(category);
    }

    @Override
    public List<Category> queryByName(String name) {
        Category category=new Category();
        category.setName(name);
        List<Category> selectbynames = categoryMapper.select(category);
        return selectbynames;
    }

    @Override
    public Boolean addCategory(Category category) {
        int i = categoryMapper.insertSelective(category);
        System.out.println(i);
        return null;
    }
}
