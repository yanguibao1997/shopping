package com.study.service;

import com.study.bo.SpuBo;
import com.study.page.PageResult;

import java.util.List;

public interface SpuService {
    public PageResult<SpuBo> querySpuByPage(Integer pageNo, Integer pageSize, String key, Boolean saleable);
}
