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

    /**
     * 根据品牌的id   来查分类
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryByBid(Long bid) {
        return categoryMapper.queryBybid(bid);
    }

    @Override
    public List<Category> queryByName(String name) {
        Category category=new Category();
        category.setName(name);
        List<Category> selectbynames = categoryMapper.select(category);
        return selectbynames;
    }

    /**
     * 来修改节点为父节点
     * @param category
     */
    @Override
    public Boolean updateCategory(Category category) {
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public Category queryById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean addCategory(Category category) {
        int i = categoryMapper.insertSelective(category);
        if(i>0){
            return true;
        }
        return false;
    }

    /**
     * 修改方法
     * @param id
     * @param name
     * @return
     */
    @Override
    public Boolean updateCategory(Long id, String name) {
        Category category=new Category();
        category.setId(id);
        category.setName(name);
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        return i>0;
    }

    @Override
    public Boolean deleteCategory(Long id) {
        int i = categoryMapper.deleteByPrimaryKey(id);
        return i>0;
    }
}
