package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 15 22:56
 **/
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
