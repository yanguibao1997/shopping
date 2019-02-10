package com.study.api;

import com.study.bo.SpuBo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/goods")
public interface GoodsApi {

    @PostMapping("/addOrUpdateGoods")
    void addGoods(@RequestBody SpuBo spuBo);

    @PutMapping("/addOrUpdateGoods")
    void updateGoods(@RequestBody SpuBo spuBo);
}
