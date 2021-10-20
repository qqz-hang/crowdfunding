package com.atguigu.crowd.service.api;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * 暴露接口
 */
@FeignClient("atguigu-crowd-redis")
public interface RedisRemoteService {

    @RequestMapping("set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key, @RequestParam("value") String value);

    @RequestMapping("set/redis/key/value/remote/timeout")
    ResultEntity<String> setRedisKeyValueRemoteTimeout(@RequestParam("key") String key,
                                                       @RequestParam("value") String value,
                                                       @RequestParam("time") long time,
                                                       @RequestParam("timeUnit") TimeUnit timeUnit);

    @RequestMapping("get/redis/string/value/remote/by/key")
    ResultEntity<String> getRedisStringValueRemoteByKey(@RequestParam("key") String key);

    @RequestMapping("remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
