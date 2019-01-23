package com.study.service.impl;

import com.study.mapper.SpecGroupMapper;
import com.study.pojo.SpecGroup;
import com.study.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Override
    public List<SpecGroup> queryBySpecGroupByCid(Long cid) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);

        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        return specGroups;
    }

    @Override
    public void addSpecGroup(SpecGroup specGroup) {
        specGroup.setId(null);
        specGroupMapper.insertSelective(specGroup);
    }

    @Override
    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    @Override
    public void deleteSpecGroup(Long id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }
}
