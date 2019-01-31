package com.study.controller;

import com.study.pojo.Sku;
import com.study.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuControl {

    @Autowired
    private SkuService skuService;

    @GetMapping("/querySkuBySpuId")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("spuId") Long spuId){
        List<Sku> skus = skuService.querySkuBySpuId(spuId);
        if(skus.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(skus);
    }
}
