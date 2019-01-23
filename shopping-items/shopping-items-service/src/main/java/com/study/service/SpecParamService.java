package com.study.service;

import com.study.pojo.SpecParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecParamService {

    public List<SpecParam> querySpecParamByCidGid(Long cid,Long gid);
}
