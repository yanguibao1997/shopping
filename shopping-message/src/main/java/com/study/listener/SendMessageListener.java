package com.study.listener;

import com.study.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SendMessageListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sh.send.message.queue",durable = "true"),
            exchange = @Exchange(value = "shopping.send.message.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = "send.message.coe"))
    public void sendMessageListener(Map<String,String> msg){
        if (msg == null || msg.size() <= 0) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            // 放弃处理
            return;
        }
        // 发送消息   需要一个验证码
//        MessageUtils.senMessage(phone,code);
        System.out.println("接受消息电话："+phone+"验证码："+code);

        // 发送失败
//        throw new RuntimeException();
    }
}
