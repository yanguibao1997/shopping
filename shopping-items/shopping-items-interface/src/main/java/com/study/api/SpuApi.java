package com.study.api;

import com.study.bo.SpuBo;
import com.study.page.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/spu")
public interface SpuApi {

    @GetMapping("/page")
    PageResult<SpuBo> querySpuPage(
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    );

    @PutMapping("/upOrDownSpuBySpuId")
    void upOrDownSpuBySpuId(@RequestParam("spuIds") List<Long> spuIds, @RequestParam("saleable") Boolean saleable);

    @DeleteMapping("/deleteSpuBySpuId")
    void deleteSpuBySpuId(@RequestParam("spuIds") List<Long> spuIds);
}
