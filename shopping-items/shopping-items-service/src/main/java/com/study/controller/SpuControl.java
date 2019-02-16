package com.study.controller;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import com.study.pojo.Spu;
import com.study.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/upOrDownSpuBySpuId")
    public ResponseEntity<Void> upOrDownSpuBySpuId(@RequestParam("spuIds") List<Long> spuIds, @RequestParam("saleable") Boolean saleable){
        spuIds.forEach( spuId -> {
            spuService.upOrDownSpuBySpuId(spuId,saleable);
        });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSpuBySpuId")
    public ResponseEntity<Void> deleteSpuBySpuId(@RequestParam("spuIds") List<Long> spuIds){
        spuIds.forEach( spuId -> {
            spuService.deleteSpuBySpuId(spuId);
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/querySpuBySpuId")
    public ResponseEntity<Spu> querySpuBySpuId(@RequestParam("spuId") Long spuId){
        Spu spu = spuService.querySpuBySpuId(spuId);
        if(spu==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(spu);
    }
}
