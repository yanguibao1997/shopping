package com.study.service.impl;

import com.study.mapper.SpuDetailMapper;
import com.study.pojo.SpuDetail;
import com.study.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetailServiceImpl implements SpuDetailService {

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public void addSpuDetail(SpuDetail spuDetail) {
        spuDetailMapper.insertSelective(spuDetail);
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        return spuDetail;
    }

    @Override
    public void updateSpuDetail(SpuDetail spuDetail) {
        spuDetailMapper.updateByPrimaryKey(spuDetail);
    }
}
