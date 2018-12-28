package com.study.service;

import com.study.pojo.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> queryByParentId(Long parentId);
}
