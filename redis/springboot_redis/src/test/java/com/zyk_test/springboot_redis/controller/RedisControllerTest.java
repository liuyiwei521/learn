package com.zyk_test.springboot_redis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyk_test.springboot_redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RedisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void setStringShouldReturnSuccessMessage() throws Exception {
        // Clean up any existing data
        redisService.deleteKey("testKey");
        
        // When & Then
        mockMvc.perform(post("/redis/string?key=testKey&value=testValue"))
                .andExpect(status().isOk())
                .andExpect(content().string("String value set successfully"));
    }

    @Test
    void setStringWithExpiryShouldReturnSuccessMessage() throws Exception {
        // Clean up any existing data
        redisService.deleteKey("testKey");
        
        // When & Then
        mockMvc.perform(post("/redis/string/expiry?key=testKey&value=testValue&timeout=10"))
                .andExpect(status().isOk())
                .andExpect(content().string("String value with expiry set successfully"));
    }

    @Test
    void getStringShouldReturnStoredValue() throws Exception {
        // Given
        redisService.setString("testKey", "testValue");

        // When & Then
        mockMvc.perform(get("/redis/string/testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));
        
        // Clean up
        redisService.deleteKey("testKey");
    }

    @Test
    void deleteKeyShouldReturnSuccessMessage() throws Exception {
        // Given
        redisService.setString("testKey", "testValue");

        // When & Then
        mockMvc.perform(delete("/redis/string/testKey"))
                .andExpect(status().isOk())
                .andExpect(content().string("Key deleted successfully"));
    }

    @Test
    void addToListLeftShouldReturnSuccessMessage() throws Exception {
        // Clean up any existing data
        redisService.deleteKey("testList");
        
        // When & Then
        mockMvc.perform(post("/redis/list/left?key=testList&value=testValue"))
                .andExpect(status().isOk())
                .andExpect(content().string("Value added to list left successfully"));
        
        // Clean up
        redisService.deleteKey("testList");
    }

    @Test
    void addToListRightShouldReturnSuccessMessage() throws Exception {
        // Clean up any existing data
        redisService.deleteKey("testList");
        
        // When & Then
        mockMvc.perform(post("/redis/list/right?key=testList&value=testValue"))
                .andExpect(status().isOk())
                .andExpect(content().string("Value added to list right successfully"));
        
        // Clean up
        redisService.deleteKey("testList");
    }

    @Test
    void getListShouldReturnStoredList() throws Exception {
        // Given
        redisService.deleteKey("testList");
        redisService.addToListRight("testList", "value1");
        redisService.addToListRight("testList", "value2");
        
        ArrayList<Object> testList = new ArrayList<>();
        testList.add("value1");
        testList.add("value2");

        // When & Then
        mockMvc.perform(get("/redis/list/testList"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testList)));
        
        // Clean up
        redisService.deleteKey("testList");
    }

    @Test
    void putHashShouldReturnSuccessMessage() throws Exception {
        // Clean up any existing data
        redisService.deleteKey("testHash");
        
        // When & Then
        mockMvc.perform(post("/redis/hash?key=testHash&hashKey=field1&value=testValue"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hash value set successfully"));
        
        // Clean up
        redisService.deleteKey("testHash");
    }

    @Test
    void getHashShouldReturnStoredValue() throws Exception {
        // Given
        redisService.deleteKey("testHash");
        redisService.putHash("testHash", "field1", "testValue");

        // When & Then
        mockMvc.perform(get("/redis/hash/testHash/field1"))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));
        
        // Clean up
        redisService.deleteKey("testHash");
    }

    @Test
    void getHashAllShouldReturnStoredMap() throws Exception {
        // Given
        redisService.deleteKey("testHash");
        redisService.putHash("testHash", "field1", "value1");
        redisService.putHash("testHash", "field2", "value2");
        
        HashMap<Object, Object> testMap = new HashMap<>();
        testMap.put("field1", "value1");
        testMap.put("field2", "value2");

        // When & Then
        mockMvc.perform(get("/redis/hash/testHash"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testMap)));
        
        // Clean up
        redisService.deleteKey("testHash");
    }
}