package io.sdb.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            log.info("【RedisLock】成功加锁 setIfAbsent >>>>>>>>>>>>>>>>>>>>  threadId= {} key = {} aoldValue = {} time= {}", Thread.currentThread().getId(),key, System.currentTimeMillis());
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            log.info("【redislock 超时解锁】currentValue={} System.currentTimeMillis()={} threadId= {} time= {}", currentValue, System.currentTimeMillis(), Thread.currentThread().getId(), System.currentTimeMillis());
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                log.info("【RedisLock】成功加锁 超时解锁 >>>>>>>>>>>>>>>>>>>>  threadId= {}", Thread.currentThread().getId());
                return true;
            }

        }

        return false;
    }

    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
                log.info("【RedisLock】unlock >>>>>>>>>>>>>>>>>>>>  threadId= {} stock = {} time= {}", Thread.currentThread().getId(), 0, System.currentTimeMillis());
                return;
            }
        } catch (Exception e) {
            log.error("【redis分布式做】解锁", e);
        }
    }

}
