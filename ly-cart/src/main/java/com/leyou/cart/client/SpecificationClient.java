package com.leyou.cart.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 25 14:53
 **/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
