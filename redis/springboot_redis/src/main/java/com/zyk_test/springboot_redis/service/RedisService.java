package com.zyk_test.springboot_redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // String operations
    public void setString(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setString(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    // List operations
    public Long addToListLeft(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long addToListRight(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Object popFromListLeft(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object popFromListRight(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // Hash operations
    public void putHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object, Object> getHashAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Long deleteHashFields(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }
}