package com.study.feignclient;

import com.study.api.SpecGroupApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-items-service")
public interface SpecGroupClient extends SpecGroupApi {

}
