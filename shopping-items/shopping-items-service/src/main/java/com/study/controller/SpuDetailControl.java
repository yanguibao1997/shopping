package com.study.controller;

import com.study.pojo.SpuDetail;
import com.study.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spuDetail")
public class SpuDetailControl {

    @Autowired
    private SpuDetailService spuDetailService;

    @GetMapping("/querySpuDetailBySpuId/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId){
        SpuDetail spuDetail = spuDetailService.querySpuDetailBySpuId(spuId);
        if(spuDetail==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spuDetail);
    }
}
