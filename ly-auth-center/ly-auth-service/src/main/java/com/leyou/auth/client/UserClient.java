package com.leyou.auth.client;

import com.leyou.item.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 20:19
 **/
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
