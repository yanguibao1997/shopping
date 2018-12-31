package com.study.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.pojo.Category;
import com.study.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Long> addCategory(@RequestBody Map map) throws IOException {
        Object data = map.get("data");
        ObjectMapper objectMapper=new ObjectMapper();
        Category category = objectMapper.readValue(data.toString(), Category.class);


        //        首先设置复建点  isParent为true
        Long parentId = category.getParentId();
        Category c = categoryService.queryById(parentId);
        c.setIsParent(true);
        Boolean parent_true = categoryService.updateCategory(c);
        if(parent_true){
            category.setId(null);
            Boolean b = categoryService.addCategory(category);
            if(b){
                return ResponseEntity.status(HttpStatus.OK).body(parentId);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
