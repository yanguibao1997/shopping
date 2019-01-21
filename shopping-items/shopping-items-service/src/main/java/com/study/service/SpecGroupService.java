package com.study.service;

import com.study.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {

    public List<SpecGroup> queryBySpecGroupByCid(Long cid);

}
