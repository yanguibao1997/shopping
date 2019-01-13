package com.study.mapper;

import com.study.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{categoryId},#{brandId})")
    public int insertCategoryBrand(@Param("categoryId") Long category_id,@Param("brandId") Long brandId);
}
