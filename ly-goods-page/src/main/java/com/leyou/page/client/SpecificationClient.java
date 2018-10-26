package com.leyou.page.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 17 16:45
 **/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
