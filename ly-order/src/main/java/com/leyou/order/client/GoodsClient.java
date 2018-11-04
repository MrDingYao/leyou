package com.leyou.order.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 11 - 03 10:39
 **/
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
