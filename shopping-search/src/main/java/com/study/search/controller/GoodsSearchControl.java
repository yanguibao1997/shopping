package com.study.search.controller;

import com.study.page.PageResult;
import com.study.search.pojo.Goods;
import com.study.search.pojo.SearchPage;
import com.study.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GoodsSearchControl {

    @Autowired
    private GoodsService goodsService;

    /**
     * 搜索商品
     * @param searchPage
     * @return
     */
    @PostMapping("/searchPage")
    public ResponseEntity<PageResult<Goods>> searchPage(@RequestBody SearchPage searchPage){
        PageResult<Goods> goodsPageResult = goodsService.searchGoodsPage(searchPage);
        if (goodsPageResult == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(goodsPageResult);
    }
}
