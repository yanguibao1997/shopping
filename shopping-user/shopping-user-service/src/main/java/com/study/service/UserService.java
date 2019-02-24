package com.study.service;

public interface UserService {

    Boolean check(String data,Integer type);

    Boolean sendMessageCode(String phone);
}
