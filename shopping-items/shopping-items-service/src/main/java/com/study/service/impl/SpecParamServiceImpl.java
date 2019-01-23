package com.study.service.impl;

import com.study.mapper.SpecParamMapper;
import com.study.pojo.SpecParam;
import com.study.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecParam> querySpecParamByCidGid(Long cid, Long gid) {
        List<SpecParam> specParams = specParamMapper.querySpecParamByCidGid(cid, gid);
        return specParams;
    }
}
