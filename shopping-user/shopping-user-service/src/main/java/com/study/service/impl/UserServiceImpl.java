package com.study.service.impl;

import com.study.mapper.UserMapper;
import com.study.pojo.User;
import com.study.service.UserService;
import com.study.utils.CodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    Logger logger= LoggerFactory.getLogger(UserService.class);

    /**
     * 查询不到手机 或者用户 才能通过验证
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean check(String data, Integer type) {
        User user=new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(user)==0;
    }

    @Override
    public Boolean sendMessageCode(String phone) {
        //        生成6位验证码
        String code = CodeUtils.generateCode(6);
        try {
//        发送短信
            Map<String,String> map=new HashMap<>();
            map.put("phone",phone);
            map.put("code",code);
            amqpTemplate.convertAndSend("shopping.send.message.exchange","send.message.coe",map);

//        需要将验证码存入redis  设置过期时间    key为  ====>   phone:code    value ======> 验证码
            stringRedisTemplate.opsForValue().set("user:code:"+phone,code,30, TimeUnit.SECONDS);
            return true;
        }catch (Exception e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }
}
