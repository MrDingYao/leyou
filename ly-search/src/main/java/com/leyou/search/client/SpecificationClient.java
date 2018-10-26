package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 13 21:10
 **/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {

}
