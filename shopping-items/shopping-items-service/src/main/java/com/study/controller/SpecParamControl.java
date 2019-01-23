package com.study.controller;

import com.study.pojo.SpecParam;
import com.study.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
