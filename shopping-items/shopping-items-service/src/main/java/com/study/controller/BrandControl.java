package com.study.controller;

import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandControl {

    @Autowired
    private BrandService brandService;

    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "isDes",defaultValue = "false") Boolean isDes,
            @RequestParam(value = "sortBy",required = false) String sortBy
    ){
        PageResult<Brand> brandPageResult = brandService.queryBrandByPage(key, pageNo, pageSize, isDes, sortBy);
        if(brandPageResult==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(brandPageResult);
    }
}
