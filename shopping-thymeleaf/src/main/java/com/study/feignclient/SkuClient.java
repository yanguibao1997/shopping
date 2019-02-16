package com.study.feignclient;

import com.study.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SkuClient extends SkuApi {

}
