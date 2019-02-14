package com.study.search.service;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.search.pojo.Goods;
import com.study.search.pojo.SearchPage;
import com.study.search.pojo.SearchPageResult;

public interface GoodsService {

    Goods goodsService(SpuBo s);

//    分页查询Goods
    SearchPageResult searchGoodsPage(SearchPage searchPage);
}
