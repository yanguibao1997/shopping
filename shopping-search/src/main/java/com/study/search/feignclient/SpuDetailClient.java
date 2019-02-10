package com.study.search.feignclient;

import com.study.api.SpuDetailApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SpuDetailClient extends SpuDetailApi{

}
