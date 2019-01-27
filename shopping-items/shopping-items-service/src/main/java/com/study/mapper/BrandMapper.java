package com.study.mapper;

import com.study.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{categoryId},#{brandId})")
    public int insertCategoryBrand(@Param("categoryId") Long category_id,@Param("brandId") Long brandId);

    @Select("select a.* from tb_brand a left join tb_category_brand b on a.id=b.brand_id where b.category_id=#{cid}")
    public List<Brand> queryBrandByCid(@Param("cid") Long cid);
}
