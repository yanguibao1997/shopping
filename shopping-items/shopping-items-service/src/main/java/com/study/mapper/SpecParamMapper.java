package com.study.mapper;

import com.study.pojo.SpecParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecParamMapper extends Mapper<SpecParam> {

    @Select("select * from tb_spec_param where cid=#{cid} and group_id=#{gid}")
    public List<SpecParam> querySpecParamByCidGidMine(@Param("cid") Long cid,@Param("gid") Long gid);
}
