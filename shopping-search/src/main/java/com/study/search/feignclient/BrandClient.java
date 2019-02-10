package com.study.search.feignclient;


import com.study.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface BrandClient extends BrandApi{

}
