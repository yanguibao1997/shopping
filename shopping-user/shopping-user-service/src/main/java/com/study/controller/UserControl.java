package com.study.controller;

import com.study.pojo.User;
import com.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserControl {

    @Autowired
    private UserService userService;

    /**
     * - 200：校验成功
     * - 400：参数有误
     * - 500：服务器内部异常
     * 校验手机 或者 用户名
     * @param data  要校验的数据
     * @param type  要校验的数据类型：1，用户名；2，手机；   默认 为1
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,@PathVariable(value = "type") Integer type){
        if(type == null){
            type=1;
        }
        Boolean check = userService.check(data, type);
        if(check==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(check);
    }

    @PostMapping("/code")
    public ResponseEntity<Void> sendMessageCode(@RequestParam("phone") String phone){
        Boolean boo = userService.sendMessageCode(phone);
        if (boo == null || !boo) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/userRegister")
    public ResponseEntity<String> userRegister(User user,String code){
        Boolean aBoolean = userService.userRegister(user, code);
        if(!aBoolean){
            return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok("注册成功");
    }

}
