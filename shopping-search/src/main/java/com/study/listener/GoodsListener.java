package com.study.listener;

import com.study.search.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    @Autowired
    private GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "shoping.create.index.queue",durable = "true"),
            exchange = @Exchange(name = "shopping.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
    public void createIndexListener(Long spuId) throws IOException {
        if (spuId == null) {
            return;
        }
//        System.out.println(spuId);
        // 创建或更新索引
        this.goodsService.createIndex(spuId);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "shoping.delete.index.queue",durable = "true"),
            exchange = @Exchange(name = "shopping.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void deleteIndexListener(Long spuId) {
        if (spuId == null) {
            return;
        }
//        System.out.println(spuId);
        // 创建或更新索引
        this.goodsService.deleteIndex(spuId);
    }
}
