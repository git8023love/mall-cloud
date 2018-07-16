package com.mall.cloud.provider.uac.service.impl;

import com.mall.cloud.provider.uac.service.RedisSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
@Service
public class RedisSetServiceImpl implements RedisSetService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Set<String> getAllValue(String key) {
        Set<String> result;
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        result = operations.members(key);
        log.info("getAllValue - 根据key获取元素. [OK] key={}, value={}", key, result);
        return result;
    }

    @Override
    public Long add(String key, String... value) {
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        Long result = operations.add(key, value);
        log.info("add - 向key里面添加元素, key={}, value={}, result={}", key, value, result);
        return result;
    }

    @Override
    public Long remove(String key, String... value) {
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        Long result = operations.remove(key,  (Object) value);
        log.info("remove - 根据key移除元素, key={}, value={}, result={}", key, value, result);
        return result;
    }
}
