package com.study.api;

import com.study.pojo.Brand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
public interface BrandApi {
    @GetMapping("/page")
    Brand queryBrandByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "isDes",defaultValue = "false") Boolean isDes,
            @RequestParam(value = "sortBy",required = false) String sortBy
    );

    @PostMapping("/addOrUpdateBrand")
    void addBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    @PutMapping("/addOrUpdateBrand")
    void updateBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    @DeleteMapping("/deleteBrand")
    void deleteBrand(@RequestParam("bids") List<Long> bids);

    @GetMapping("/queryBrandByCid")
    Brand queryBrandByCid(@RequestParam("cid") Long cid);

    @GetMapping("/queryBrandById")
    Brand queryBrandById(@RequestParam("bid") Long bid);
}
