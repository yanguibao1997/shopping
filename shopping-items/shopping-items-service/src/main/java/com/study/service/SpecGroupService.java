package com.study.service;

import com.study.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {

    public List<SpecGroup> queryBySpecGroupByCid(Long cid);

    public void  addSpecGroup(SpecGroup specGroup);

    public void  updateSpecGroup(SpecGroup specGroup);

    public void  deleteSpecGroup(Long id);

}
