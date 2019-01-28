package com.study.service;

import com.study.pojo.SpecParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecParamService {

    public List<SpecParam> querySpecParamByCidGid(Long cid,Long gid);

    public void addSpecParam(SpecParam specParam);

    public void updateSpecParam(SpecParam specParam);

    public void deleteSpecParam(Long id);

    public List<SpecParam> querySpecParamByCidGidMine(Long cid, Long gid);
}
