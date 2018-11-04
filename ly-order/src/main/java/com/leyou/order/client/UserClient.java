package com.leyou.order.client;

import com.leyou.item.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 11 - 03 10:59
 **/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
