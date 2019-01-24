package com.study.controller;

import com.study.pojo.SpecParam;
import com.study.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecParamControl {

    @Autowired
    private SpecParamService specParamService;

    @GetMapping("/querySpecParamByCidGid")
    public ResponseEntity<List<SpecParam>> querySpecParamByCidGid(
            @RequestParam ("cid") Long cid,
            @RequestParam("gid") Long gid
    ){
        List<SpecParam> specParams = specParamService.querySpecParamByCidGid(cid, gid);
        return ResponseEntity.ok(specParams);
    }

    @PostMapping("/addOrUpdateSpecParam")
    public ResponseEntity<Void> addSpecParam(SpecParam specParam){
        specParamService.addSpecParam(specParam);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/addOrUpdateSpecParam")
    public ResponseEntity<Void> updateSpecParam(SpecParam specParam){
        System.out.println(specParam);
        specParamService.updateSpecParam(specParam);
        return ResponseEntity.ok().build();
    }
}
