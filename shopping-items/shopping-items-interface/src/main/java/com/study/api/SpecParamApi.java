package com.study.api;

import com.study.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/specification")
public interface SpecParamApi {
    @GetMapping("/querySpecParamByCidGid")
    List<SpecParam> querySpecParamByCidGid(
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "searching",required = false) Boolean searching
    );

    @PostMapping("/addOrUpdateSpecParam")
    void addSpecParam(SpecParam specParam);


    @PutMapping("/addOrUpdateSpecParam")
    void updateSpecParam(SpecParam specParam);

    @DeleteMapping("/deleteSpecParam")
    void deleteSpecParam(@RequestParam("ids") List<Long> ids);
}
