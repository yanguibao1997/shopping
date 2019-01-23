package com.study.controller;

import com.study.pojo.SpecGroup;
import com.study.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specification")
public class SpecGroupControl {

    @Autowired
    private SpecGroupService specGroupService;

    @GetMapping("/querySpecGroupByCid/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroups = specGroupService.queryBySpecGroupByCid(cid);
        return ResponseEntity.ok(specGroups);
    }

    @PostMapping("/addOrUpdateSpecGroup")
    @Transactional
    public ResponseEntity<Void> addSpecGroup(SpecGroup specGroup){
        specGroupService.addSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/addOrUpdateSpecGroup")
    @Transactional
    public ResponseEntity<Void> updateSpecGroup(SpecGroup specGroup){
        specGroupService.updateSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/deleteSpecGroup")
    @Transactional
    public ResponseEntity<Void> deleteSpecGroup(@RequestParam("ids") List<Long> ids){

        ids.forEach(id -> {
            specGroupService.deleteSpecGroup(id);
        });
        return ResponseEntity.ok().build();
    }
}
