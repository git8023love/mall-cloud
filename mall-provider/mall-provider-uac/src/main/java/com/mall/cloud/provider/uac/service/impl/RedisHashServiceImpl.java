package com.mall.cloud.provider.uac.service.impl;

import com.google.common.collect.Lists;
import com.mall.cloud.provider.uac.service.RedisHashService;
import com.mall.cloud.uac.common.util.PublicUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Service
public class RedisHashServiceImpl implements RedisHashService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public <T> List<T> getValueByFields(String key, Set<String> fields) {
        HashOperations<String, String, T> operations = stringRedisTemplate.opsForHash();
        if (!stringRedisTemplate.hasKey(key)) {
            return Collections.emptyList();
        }
        List<T> values = operations.multiGet(key, fields);
        if (PublicUtil.isEmpty(values)) {
            return Collections.emptyList();
        }
        log.info("getValueByFields - 根据key获取所有给定字段的值. [OK] key={}, fields={}, values={}", key, fields, values);
        return values;

    }

    @Override
    public <T> List<T> getValueByField(String key, String field) {
        HashOperations<String, String, T> operations = stringRedisTemplate.opsForHash();
        if (!stringRedisTemplate.hasKey(key)) {
            return Collections.emptyList();
        }
        T value  = operations.get(key, field);
        if (PublicUtil.isEmpty(value)) {
            return Collections.emptyList();
        }
        List<T> values = Lists.newArrayList();
        values.add(value);
        log.info("getValueByField - 根据key获取给定字段的值. [OK] key={}, field={}, values={}", key, field, values);
        return values;
    }

    @Override
    public void setValueByFields(String key, Map<String, Object> map) {
        HashOperations<String, String, Object> operations = stringRedisTemplate.opsForHash();
        operations.putAll(key, map);
        log.info("setValueByFields - 同时将多个 field-value (域-值)对设置到哈希表 key 中. [ok] key={}, map={}", key, map);
    }

    @Override
    public Long removeFields(String key, String... hashKeys) {
        HashOperations<String, String, Object> operations = stringRedisTemplate.opsForHash();
        Long result = operations.delete(key, (Object) hashKeys);
        log.info("removeFields- 删除一个或多个哈希表字段. [OK] key={}, hashKeys={}, result={}", key, hashKeys, result);
        return result;
    }
}
