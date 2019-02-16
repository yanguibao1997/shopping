package com.study.feignclient;

import com.study.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface CategoryClient extends CategoryApi{
}
