package com.zyk_test.springboot_redis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    void testStringOperations() {
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
    void testStringOperationsWithExpiry() {
        String key = "test:string:expiry:key";
        String value = "testValue";
        long timeout = 2; // 2 seconds
        
        // Test set with expiry
        redisService.setString(key, value, timeout, TimeUnit.SECONDS);
        Object retrievedValue = redisService.getString(key);
        assertThat(retrievedValue).isEqualTo(value);
        
        // Test has key
        Boolean exists = redisService.hasKey(key);
        assertThat(exists).isTrue();
        
        // Wait for expiry
        try {
            Thread.sleep((timeout + 1) * 1000); // Sleep for timeout + 1 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify expiry
        exists = redisService.hasKey(key);
        assertThat(exists).isFalse();
    }
    
    @Test
    void testListOperations() {
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
    void testHashOperations() {
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
    
    @Test
    void testPopFromListRight() {
        String key = "test:list:rightpop:key";
        String value1 = "value1";
        String value2 = "value2";
        
        // Clean up any existing data
        redisService.deleteKey(key);
        
        // Test add to list
        redisService.addToListLeft(key, value1);
        redisService.addToListLeft(key, value2);
        
        // Test pop from right
        Object poppedValue = redisService.popFromListRight(key);
        assertThat(poppedValue).isEqualTo(value1);
        
        // Clean up
        redisService.deleteKey(key);
    }
    
    @Test
    void testAddToListLeft() {
        String key = "test:list:leftpush:key";
        String value = "value";
        
        // Clean up any existing data
        redisService.deleteKey(key);
        
        // Test add to list left
        Long result = redisService.addToListLeft(key, value);
        assertThat(result).isEqualTo(1L);
        
        // Verify
        List<Object> list = redisService.getList(key);
        assertThat(list).containsExactly(value);
        
        // Clean up
        redisService.deleteKey(key);
    }
}