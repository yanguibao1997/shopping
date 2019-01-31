package com.study.service;

import com.study.pojo.SpuDetail;

public interface SpuDetailService {
    public void addSpuDetail(SpuDetail spuDetail);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    void updateSpuDetail(SpuDetail spuDetail);
}
