package com.study.service;

import com.study.pojo.Stock;

public interface StockService {

    public void addStock(Stock stock);

    void deleteStockBySkuId(Long skuId);

    Stock queryStockBySkuId(Long skuId);
}
