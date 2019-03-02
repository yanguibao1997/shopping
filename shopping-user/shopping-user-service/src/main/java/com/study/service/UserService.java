package com.study.service;

import com.study.pojo.User;

public interface UserService {

    Boolean check(String data,Integer type);

    Boolean sendMessageCode(String phone);

    Boolean userRegister(User user,String code);

    User login(String userName,String password);
}
