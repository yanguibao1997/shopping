package com.study.service;

import com.study.pojo.Sku;

import java.util.List;

public interface SkuService {

    public void addSku(List<Sku> skus,Long skuId);

    List<Sku> querySkuBySpuId(Long spuId);

    void deleteSkuBySpuId(Long spuId);
}
