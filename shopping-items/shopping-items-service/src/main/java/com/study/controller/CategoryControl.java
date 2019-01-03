package com.study.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.pojo.Category;
import com.study.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<Category> addCategory(@RequestBody Map map) throws IOException {
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
                return ResponseEntity.status(HttpStatus.OK).body(category);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/updatecategory")
    public ResponseEntity<String> updateCategory(
            @RequestParam("id") Long id,
            @RequestParam("name") String name
     ){
        Boolean b = categoryService.updateCategory(id, name);
        if(b){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("修改成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("修改失败");
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteCategory(
            @RequestParam("id") Long id,
            @RequestParam("parentId") Long parentId
    ){
        Boolean b = categoryService.deleteCategory(id);
        if(b){
//            删除成功后  如果子节点的数目为0   则将父节点设为文件
            int size = categoryService.queryByParentId(parentId).size();
            if(size>0){
                ResponseEntity.status(HttpStatus.ACCEPTED).body("删除成功");
            }else{
//                设置父节点为文件
                Category category = categoryService.queryById(parentId);
                category.setIsParent(false);
                Boolean bo = categoryService.updateCategory(category);
                if(bo){
                    ResponseEntity.status(HttpStatus.ACCEPTED).body("删除成功");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("删除失败");
    }
}
