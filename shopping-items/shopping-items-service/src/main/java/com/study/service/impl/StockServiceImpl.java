package com.study.service.impl;

import com.study.mapper.StockMapper;
import com.study.pojo.Stock;
import com.study.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public void addStock(Stock stock) {
        stockMapper.insertSelective(stock);
    }

    @Override
    public void deleteStockBySkuId(Long skuId) {
        stockMapper.deleteByPrimaryKey(skuId);
    }

    @Override
    public Stock queryStockBySkuId(Long skuId) {
        Stock stock = stockMapper.selectByPrimaryKey(skuId);
        return stock;
    }
}
