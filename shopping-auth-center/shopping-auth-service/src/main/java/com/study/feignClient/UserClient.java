package com.study.feignClient;

import com.study.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shopping-user-service")
public interface UserClient extends UserApi{
}
