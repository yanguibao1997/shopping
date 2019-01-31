package com.study.service.impl;

import com.study.bo.SpuBo;
import com.study.pojo.Sku;
import com.study.pojo.Spu;
import com.study.pojo.SpuDetail;
import com.study.pojo.Stock;
import com.study.service.*;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuService spuService;

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockService stockService;

    @Override
    @Transactional
    public void addGoods(SpuBo spuBo) {
//        其中 spuBo  中有   spu  spuDetail  sku集合  库存的一些信息
//        首先添加商品集 ————————>  之后得到spuId 再进行下面的操作

//        添加spu 商品集
        Spu spu=spuBo;
        spuService.addSpu(spu);

//        更新后------> 获得id

//        spuDetail增加
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailService.addSpuDetail(spuBo.getSpuDetail());

//        添加SKU信息--->  是多个SKU的集合 和对应的库存
        List<Sku> skus = spuBo.getSkus();
        skuService.addSku(skus,spu.getId());

    }

    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
//        先将原来的Sku 删除,包括库存  ---->根据 spuId删除 sku ,根据skuId 删除 库存stock
        Spu spu=spuBo;

        Long spuId = spuBo.getSpuDetail().getSpuId();
        this.deleteOldSkuStock(spuId);

//        插入新的SKU 包括  库存
        List<Sku> skus = spuBo.getSkus();
        skuService.addSku(skus,spu.getId());

//        跟新 SPU商品集
        spuService.updateSpu(spu);

//        跟新 spuDetail
        spuDetailService.updateSpuDetail(spuBo.getSpuDetail());
    }

    /**
     * 根据 spuId删除 sku ,根据skuId 删除 库存stock
     * @param spuId
     */
    private void deleteOldSkuStock(Long spuId){
//        查询出所有的Sku  删除库存
        List<Sku> skus = skuService.querySkuBySpuId(spuId);
        skus.forEach( sku -> {
//            删除库存
            Long skuId = sku.getId();
            stockService.deleteStockBySkuId(skuId);
        });

//        删除库存
        skuService.deleteSkuBySpuId(spuId);
    }
}
