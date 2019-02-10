package com.study.search.feignclient;

import com.study.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SkuClient extends SkuApi {

}
