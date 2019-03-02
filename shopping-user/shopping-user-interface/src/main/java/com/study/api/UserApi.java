package com.study.api;

import com.study.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @GetMapping("/check/{data}/{type}")
    Boolean check(@PathVariable("data") String data, @PathVariable(value = "type") Integer type);

    @PostMapping("/code")
    void sendMessageCode(@RequestParam("phone") String phone);

    @PostMapping("/userRegister")
    String userRegister(User user,@RequestParam("code") String code);

    @PostMapping("/login")
    User login(@RequestParam("userName") String userName,@RequestParam("password") String password);
}
