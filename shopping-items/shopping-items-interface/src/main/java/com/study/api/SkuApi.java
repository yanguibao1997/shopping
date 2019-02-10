package com.study.api;

import com.study.pojo.Sku;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/sku")
public interface SkuApi {

    @GetMapping("/querySkuBySpuId")
    List<Sku> querySkuBySpuId(@RequestParam("spuId") Long spuId);
}
