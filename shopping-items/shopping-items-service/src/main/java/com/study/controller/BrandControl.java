package com.study.controller;

import com.study.mapper.BrandMapper;
import com.study.page.PageResult;
import com.study.pojo.Brand;
import com.study.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandControl {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandMapper brandMapper;


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

    @PostMapping("/addOrUpdateBrand")
    @Transactional
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.addBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/addOrUpdateBrand")
    @Transactional
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        System.out.println(brand.getId());

//        先根据品牌ID将原来中间表对应数据删除
        brandService.deleteCategoryBrandByBid(brand.getId());
//        根据id修改品牌
        brandService.updateBrandByid(brand);
//        插入中间分类
        cids.forEach( cid -> {
//            添加数据到  关联表
            brandMapper.insertCategoryBrand(cid,brand.getId());
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/deleteBrand")
    @Transactional
    public ResponseEntity<Void> deleteBrand(@RequestParam("bids") List<Long> bids){
        System.out.println(bids);

        bids.forEach(bid -> {
            brandService.deleteBrandById(bid);

//      先根据品牌ID将原来中间表对应数据删除
            brandService.deleteCategoryBrandByBid(bid);
        });
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/queryBrandByCid")
    public ResponseEntity<List<Brand>> queryBrandByCid(@RequestParam("cid") Long cid){
        List<Brand> brands = brandService.queryBrandByCid(cid);
        if(brands==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(brands);
    }
}
