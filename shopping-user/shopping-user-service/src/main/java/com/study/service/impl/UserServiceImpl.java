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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
            stringRedisTemplate.opsForValue().set("user:code:"+phone,code,5, TimeUnit.MINUTES);
            return true;
        }catch (Exception e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    @Override
    @Transactional
    public Boolean userRegister(User user, String code) {
//        首先从redis中获得验证码     获得完删除
        String codeKey="user:code:"+user.getPhone();
        String s = stringRedisTemplate.opsForValue().get(codeKey);
        if(!s.equals(code)){
//            验证码错误
            return false;
        }
//        设置盐
        String slat = CodeUtils.generateSalt();
        user.setSalt(slat);
        user.setId(null);
        user.setCreated(new Date());
//        生成密码
        String pssword = CodeUtils.md5Hex(user.getPassword(), slat);
        user.setPassword(pssword);

//        设置手机号
        user.setPhone(user.getPhone());
//        插入数据库
        Boolean b=userMapper.insertSelective(user)>0;
        // 如果注册成功，删除redis中的code
        if (b) {
            try {
                this.stringRedisTemplate.delete(codeKey);
                return true;
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code：{}", code, e);
            }
        }
        return false;
    }

    @Override
    public User login(String userName, String password) {
//        根据用户名查询用户
        User user=new User();
        user.setUsername(userName);
        List<User> select = userMapper.select(user);
        User u;
        if(null != select && select.size()>0){
            u=select.get(0);
//            验证密码

            String p = u.getPassword();
            String salt = u.getSalt();

            String s = CodeUtils.md5Hex(password, salt);
            if(s.equals(p)){
//                登录成功
                return u;
            }else{
                logger.error("密码不正确");
            }
        }else{
            logger.error("无此用户：{}",userName);
        }
        return null;
    }
}
