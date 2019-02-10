package com.study.search.feignclient;

import com.study.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SpuClient extends SpuApi {

}
