package com.study.search.feignclient;

import com.study.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface GoodsClient extends GoodsApi {

}
