package com.leyou.search.listener;

import com.leyou.search.service.ISearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 19 20:51
 **/
@Component
public class GoodsListener {

    @Autowired
    private ISearchService searchService;

    /**
     * 处理insert和update消息
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.index.queue",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}))
    public void listenCreate(Long id){
        if (id == null) {
            return;
        }
        // 调用service修改索引库
        this.searchService.createIndex(id);
    }

    /**
     * 监听delete消息
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void listenDelete(Long id){
        this.searchService.deleteIndex(id);
    }
}
