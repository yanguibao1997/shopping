package com.study.controller;

import com.study.pojo.Category;
import com.study.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/isname")
    public ResponseEntity<String> isNodeName(@RequestParam(value = "name")String name){
        List<Category> categories = categoryService.queryByName(name);
        if(categories==null || categories.size()==0){
//            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return ResponseEntity.status(HttpStatus.OK).body("true");
        }
        return ResponseEntity.status(HttpStatus.OK).body("false");
    }

    @PostMapping("/addcategory")
    public ResponseEntity<Boolean> addCategory(@RequestBody Category category){
        Boolean b = categoryService.addCategory(category);
        if(b){
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
    }
}
