package com.leyou.page.listener;

import com.leyou.page.service.IGoodsService;
import com.leyou.page.service.impl.FileService;
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
 * @Date 2018 - 10 - 20 10:15
 **/
@Component
public class GoodsListener {

    @Autowired
    private FileService fileService;

    /**
     * 监听insert和update消息创建静态页面
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.page.queue",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}))
    public void listenCteate(Long id) throws Exception {
        if (id == null) {
            return;
        }
        this.fileService.createHtml(id);
    }

    /**
     * 监听delete消息，删除静态页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.page.queue",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void listenDelete(Long id){
        this.fileService.deleteHtml(id);
    }
}
