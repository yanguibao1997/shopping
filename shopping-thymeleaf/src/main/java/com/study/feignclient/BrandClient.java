package com.study.feignclient;

import com.study.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface BrandClient extends BrandApi{

}
