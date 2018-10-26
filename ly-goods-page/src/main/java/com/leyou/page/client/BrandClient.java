package com.leyou.page.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 16:44
 **/
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
