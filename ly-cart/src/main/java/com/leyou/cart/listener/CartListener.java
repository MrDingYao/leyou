package com.leyou.cart.listener;

import com.leyou.cart.service.ICartService;
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
public class CartListener {

    @Autowired
    private ICartService cartService;

    /**
     * 监听delete消息，删除购物车中对应的商品
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.cart.queue",durable = "true"),
            exchange = @Exchange(value = "ly.cart.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "cart.delete"))
    public void listenDelete(Long id){
        this.cartService.deleteCart(id);
    }
}
