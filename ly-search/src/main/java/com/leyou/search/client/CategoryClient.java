package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 13 19:36
 **/
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
