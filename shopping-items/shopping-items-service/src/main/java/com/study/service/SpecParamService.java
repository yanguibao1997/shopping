package com.study.service;

import com.study.pojo.SpecParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecParamService {

    public List<SpecParam> querySpecParamByCidGid(Long cid,Long gid,Boolean searching);

    public void addSpecParam(SpecParam specParam);

    public void updateSpecParam(SpecParam specParam);

    public void deleteSpecParam(Long id);
}
