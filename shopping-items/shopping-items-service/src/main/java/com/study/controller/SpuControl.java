package com.study.controller;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spu")
public class SpuControl {

    @Autowired
    private SpuService spuService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuPage(
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ){
        PageResult<SpuBo> spuBoPageResult = spuService.querySpuByPage(pageNo, pageSize, key, saleable);
        if(spuBoPageResult==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(spuBoPageResult);
    }
}
