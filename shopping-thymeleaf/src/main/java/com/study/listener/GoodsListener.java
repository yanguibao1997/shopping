package com.study.listener;

import com.study.service.FileService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private FileService fileService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "shopping.create.page.queue",durable = "true"),
            exchange = @Exchange(value = "shopping.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void createPageListener(Long spuId) throws Exception {
        if (spuId == null) {
            return;
        }
//        System.out.println(spuId);
        fileService.createHtml(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "shopping.delete.page.queue",durable = "true"),
            exchange = @Exchange(value = "shopping.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void deletePageListener(Long spuId){
        if (spuId == null) {
            return;
        }
//        System.out.println(spuId);
        fileService.deleteHtml(spuId);
    }
}
