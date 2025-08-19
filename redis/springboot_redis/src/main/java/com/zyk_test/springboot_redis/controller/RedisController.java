package com.zyk_test.springboot_redis.controller;

import com.zyk_test.springboot_redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    // String operations
    @PostMapping("/string")
    public String setString(@RequestParam String key, @RequestParam String value) {
        redisService.setString(key, value);
        return "String value set successfully";
    }

    @PostMapping("/string/expiry")
    public String setStringWithExpiry(@RequestParam String key, @RequestParam String value, 
                                      @RequestParam long timeout) {
        redisService.setString(key, value, timeout, TimeUnit.SECONDS);
        return "String value with expiry set successfully";
    }

    @GetMapping("/string/{key}")
    public Object getString(@PathVariable String key) {
        return redisService.getString(key);
    }

    @DeleteMapping("/string/{key}")
    public String deleteKey(@PathVariable String key) {
        redisService.deleteKey(key);
        return "Key deleted successfully";
    }

    // List operations
    @PostMapping("/list/left")
    public String addToListLeft(@RequestParam String key, @RequestParam String value) {
        redisService.addToListLeft(key, value);
        return "Value added to list left successfully";
    }

    @PostMapping("/list/right")
    public String addToListRight(@RequestParam String key, @RequestParam String value) {
        redisService.addToListRight(key, value);
        return "Value added to list right successfully";
    }

    @GetMapping("/list/{key}")
    public List<Object> getList(@PathVariable String key) {
        return redisService.getList(key);
    }

    // Hash operations
    @PostMapping("/hash")
    public String putHash(@RequestParam String key, @RequestParam String hashKey, 
                          @RequestParam String value) {
        redisService.putHash(key, hashKey, value);
        return "Hash value set successfully";
    }

    @GetMapping("/hash/{key}/{hashKey}")
    public Object getHash(@PathVariable String key, @PathVariable String hashKey) {
        return redisService.getHash(key, hashKey);
    }

    @GetMapping("/hash/{key}")
    public Map<Object, Object> getHashAll(@PathVariable String key) {
        return redisService.getHashAll(key);
    }
}