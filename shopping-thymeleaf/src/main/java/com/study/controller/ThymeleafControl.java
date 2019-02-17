package com.study.controller;

import com.study.service.FileService;
import com.study.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/item")
public class ThymeleafControl {

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private FileService fileService;


    @GetMapping("/{spuId}.html")
    public String loadThymeleaf(@PathVariable("spuId") Long spuId, Model model){

        Map<String, Object> allMap = thymeleafService.loadThymeleaf(spuId);
        model.addAllAttributes(allMap);

        // 判断是否需要生成新的页面
        if(!this.fileService.exists(spuId)){
            this.fileService.syncCreateHtml(spuId);
        }

        return "item";
    }
}
