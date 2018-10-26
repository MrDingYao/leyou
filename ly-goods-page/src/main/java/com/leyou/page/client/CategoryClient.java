package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 16:43
 **/
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
