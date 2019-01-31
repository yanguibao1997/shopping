package com.study.service.impl;

import com.study.mapper.SkuMapper;
import com.study.pojo.Sku;
import com.study.pojo.Stock;
import com.study.service.SkuService;
import com.study.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockService stockService;

    /**
     * 多商品添加
     * @param skus
     */
    @Override
    public void addSku(List<Sku> skus,Long spuId) {
//        进行商品添加
        skus.forEach( sku -> {
            if(!sku.getEnable()){
                return;
            }
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            skuMapper.insertSelective(sku);

//            进行库存添加
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockService.addStock(stock);
        });
    }


    @Override
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku=new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
//        将库存也一并查询
        skus.forEach( (Sku s) -> {
            Stock stock = stockService.queryStockBySkuId(s.getId());

            s.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    public void deleteSkuBySpuId(Long spuId) {
        Sku sku=new Sku();
        sku.setSpuId(spuId);
        skuMapper.delete(sku);
    }
}
