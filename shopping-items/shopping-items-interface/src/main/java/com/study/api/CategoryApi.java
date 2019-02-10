package com.study.api;

import com.study.pojo.Category;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("/list")
    List<Category> queryByParentId(@RequestParam(value = "pid",defaultValue = "0") Long parentId);


    @GetMapping("/queryByBid/{bid}")
    List<Category> queryByBid(@PathVariable("bid") Long bid);


    @GetMapping("/isname")
    String isNodeName(@RequestParam(value = "name")String name);

    @PostMapping("/addcategory")
    Category addCategory(Category category) throws IOException;

    @PutMapping("/updatecategory")
    String updateCategory(
            @RequestParam("id") Long id,
            @RequestParam("name") String name
    );

    @DeleteMapping("/deleteCategory")
    String deleteCategory(
            @RequestParam("id") Long id,
            @RequestParam("parentId") Long parentId
    );


    @GetMapping("/queryCategoryNameByIds")
    List<String> queryCategoryNameByIds(@RequestParam("ids") List<Long> ids);
}
