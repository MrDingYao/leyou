package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 20:28
 **/
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
