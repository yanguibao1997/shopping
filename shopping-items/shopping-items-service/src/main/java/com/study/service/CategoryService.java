package com.study.service;

import com.study.pojo.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> queryByParentId(Long parentId);

    public List<Category> queryByName(String name);

    public Boolean updateCategory(Category category);

    public Category queryById(Long id);

    public Boolean addCategory(Category category);

    public Boolean updateCategory(Long id,String name);

    public Boolean deleteCategory(Long id);
}
