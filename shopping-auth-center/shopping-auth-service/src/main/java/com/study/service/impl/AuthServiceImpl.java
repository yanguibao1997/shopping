package com.study.service.impl;

import com.study.controller.AuthControl;
import com.study.entiy.UserInfo;
import com.study.feignClient.UserClient;
import com.study.pojo.User;
import com.study.properties.JwtProperties;
import com.study.service.AuthService;
import com.study.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    Logger logger= LoggerFactory.getLogger(AuthControl.class);

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String createToken(String userName, String passord) {
        try {
            //        查询用户
            User user = userClient.login(userName, passord);

            if(null == user){
                logger.error("无此用户信息");
                return null;
            }

//        有用户   生成Token
            UserInfo userInfo=new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            return token;
        } catch (Exception e) {
            return null;
        }
    }
}
