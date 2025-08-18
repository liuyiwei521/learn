package com.zyk_test.springboot_redis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testStringOperations() {
        String key = "test:string:key";
        String value = "testValue";
        
        // Test set and get
        redisService.setString(key, value);
        Object retrievedValue = redisService.getString(key);
        assertThat(retrievedValue).isEqualTo(value);
        
        // Test has key
        Boolean exists = redisService.hasKey(key);
        assertThat(exists).isTrue();
        
        // Test delete
        Boolean deleted = redisService.deleteKey(key);
        assertThat(deleted).isTrue();
        
        // Verify deletion
        exists = redisService.hasKey(key);
        assertThat(exists).isFalse();
    }
    
    @Test
    public void testListOperations() {
        String key = "test:list:key";
        String value1 = "value1";
        String value2 = "value2";
        
        // Clean up any existing data
        redisService.deleteKey(key);
        
        // Test add to list
        redisService.addToListRight(key, value1);
        redisService.addToListRight(key, value2);
        
        // Test get list
        List<Object> list = redisService.getList(key);
        assertThat(list).containsExactly(value1, value2);
        
        // Test pop from list
        Object poppedValue = redisService.popFromListLeft(key);
        assertThat(poppedValue).isEqualTo(value1);
        
        // Clean up
        redisService.deleteKey(key);
    }
    
    @Test
    public void testHashOperations() {
        String key = "test:hash:key";
        String hashKey1 = "field1";
        String hashKey2 = "field2";
        String value1 = "value1";
        String value2 = "value2";
        
        // Clean up any existing data
        redisService.deleteKey(key);
        
        // Test put hash
        redisService.putHash(key, hashKey1, value1);
        redisService.putHash(key, hashKey2, value2);
        
        // Test get hash field
        Object fieldValue = redisService.getHash(key, hashKey1);
        assertThat(fieldValue).isEqualTo(value1);
        
        // Test get all hash
        Map<Object, Object> allFields = redisService.getHashAll(key);
        assertThat(allFields).containsEntry(hashKey1, value1);
        assertThat(allFields).containsEntry(hashKey2, value2);
        
        // Test delete hash fields
        Long deletedCount = redisService.deleteHashFields(key, hashKey1);
        assertThat(deletedCount).isEqualTo(1L);
        
        // Clean up
        redisService.deleteKey(key);
    }
}