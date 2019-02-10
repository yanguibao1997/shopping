package com.study.service.impl;

import com.study.mapper.SpecParamMapper;
import com.study.pojo.SpecParam;
import com.study.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService{

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecParam> querySpecParamByCidGid(Long cid, Long gid, Boolean searching) {
        SpecParam specParam=new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        if(searching!=null){
            specParam.setSearching(searching);
        }
        List<SpecParam> specParams = specParamMapper.select(specParam);
        return specParams;
    }

    @Override
    public void addSpecParam(SpecParam specParam) {
        specParamMapper.insertSelective(specParam);
    }

    @Override
    public void updateSpecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    @Override
    public void deleteSpecParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }
}
