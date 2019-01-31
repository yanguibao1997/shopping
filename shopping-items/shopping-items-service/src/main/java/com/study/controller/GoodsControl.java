package com.study.controller;

import com.study.bo.SpuBo;
import com.study.pojo.Sku;
import com.study.pojo.Spu;
import com.study.pojo.SpuDetail;
import com.study.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsControl {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/addOrUpdateGoods")
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spuBo){
//        进行一系列的逻辑   进行商品储存
        goodsService.addGoods(spuBo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/addOrUpdateGoods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        goodsService.updateGoods(spuBo);
        return ResponseEntity.ok().build();
    }
}
