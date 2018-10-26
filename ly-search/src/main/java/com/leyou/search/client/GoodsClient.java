package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description // 继承item-interface中提供的接口
 * @Author santu
 * @Date 2018 - 10 - 12 15:47
 **/
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {


}
