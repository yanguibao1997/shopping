package com.study.mapper;

import com.study.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {

    @Select("select id,name,parent_id as parentId,is_parent as isParent,sort from tb_category a where exists (select 1 from tb_category_brand b where a.id=b.category_id and brand_id=#{bid})")
    public List<Category> queryBybid(@Param("bid") Long bid);

}
