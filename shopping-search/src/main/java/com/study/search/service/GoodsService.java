package com.study.search.service;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.search.pojo.Goods;
import com.study.search.pojo.SearchPage;

public interface GoodsService {

    Goods goodsService(SpuBo s);

//    分页查询Goods
    PageResult<Goods> searchGoodsPage(SearchPage searchPage);
}
