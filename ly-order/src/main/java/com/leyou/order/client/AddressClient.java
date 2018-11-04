package com.leyou.order.client;

import com.leyou.item.api.AddressApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 11 - 03 10:58
 **/
@FeignClient("user-service")
public interface AddressClient extends AddressApi {
}
