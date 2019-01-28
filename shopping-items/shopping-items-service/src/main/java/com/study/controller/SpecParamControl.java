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
            @RequestParam (value = "cid",required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid
    ){
        List<SpecParam> specParams=null;
        if(cid!=null && gid!=null){
            specParams = specParamService.querySpecParamByCidGidMine(cid, gid);
        }else {
            specParams = specParamService.querySpecParamByCidGid(cid, gid);
        }

        return ResponseEntity.ok(specParams);
    }

    @PostMapping("/addOrUpdateSpecParam")
    public ResponseEntity<Void> addSpecParam(SpecParam specParam){
        specParamService.addSpecParam(specParam);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/addOrUpdateSpecParam")
    public ResponseEntity<Void> updateSpecParam(SpecParam specParam){
        specParamService.updateSpecParam(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSpecParam")
    public ResponseEntity<Void> deleteSpecParam(@RequestParam("ids") List<Long> ids){
        ids.forEach( id -> {
            specParamService.deleteSpecParam(id);
        });
        return ResponseEntity.ok().build();
    }
}
