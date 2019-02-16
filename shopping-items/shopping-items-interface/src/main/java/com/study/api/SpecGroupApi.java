package com.study.api;

import com.study.pojo.SpecGroup;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/specification")
public interface SpecGroupApi {

    @GetMapping("/querySpecGroupByCid/{cid}")
    List<SpecGroup> querySpecGroupByCid(@PathVariable("cid") Long cid);

    @PostMapping("/addOrUpdateSpecGroup")
    void addSpecGroup(SpecGroup specGroup);

    @PutMapping("/addOrUpdateSpecGroup")
    void updateSpecGroup(SpecGroup specGroup);

    @DeleteMapping("/deleteSpecGroup")
    void deleteSpecGroup(@RequestParam("ids") List<Long> ids);
}
