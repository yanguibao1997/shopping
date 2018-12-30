package com.study.controller;

import com.study.pojo.Category;
import com.study.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryControl {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long parentId){
        List<Category> categories = categoryService.queryByParentId(parentId);
        if(categories==null || categories.size()<1){
//            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categories);
    }


    /*@GetMapping("/list")
    public MappingJacksonValue queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long parentId,String callback){
        List<Category> categories = categoryService.queryByParentId(parentId);

        MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(categories);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }*/
}
