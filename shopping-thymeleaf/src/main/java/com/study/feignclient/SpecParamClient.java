package com.study.feignclient;

import com.study.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SpecParamClient extends SpecParamApi{

}
