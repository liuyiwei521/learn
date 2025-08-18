# Spring Boot Redis 示例项目

本项目演示了如何在Spring Boot应用程序中集成和使用Redis数据库。项目实现了基本的Redis操作，包括字符串、列表和哈希数据结构的操作，并提供了完整的REST API接口。

## 功能特性

- Redis连接配置
- 字符串操作（设置、获取、删除、设置过期时间）
- 列表操作（添加、获取、弹出元素）
- 哈希操作（设置字段、获取字段、获取所有字段、删除字段）
- RESTful API接口

## 技术栈

- Spring Boot 3.5.4
- Spring Data Redis
- Redis Lettuce客户端
- Maven 3+
- Java 21

## 实现步骤

### 1. 项目依赖

项目使用了以下核心依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

### 2. Redis配置

在[application.properties](file:///Users/zhangyekai/IdeaProjects/learn/redis/springboot_redis/src/main/resources/application.properties)中配置Redis连接参数：

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=2000ms
```

### 3. RedisTemplate配置

创建[RedisConfig.java](file:///Users/zhangyekai/IdeaProjects/learn/redis/springboot_redis/src/main/java/com/zyk_test/springboot_redis/config/RedisConfig.java)配置类，配置RedisTemplate以支持字符串和JSON序列化：

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    // ...
    return template;
}
```

### 4. 服务层实现

创建[RedisService.java](file:///Users/zhangyekai/IdeaProjects/learn/redis/springboot_redis/src/main/java/com/zyk_test/springboot_redis/service/RedisService.java)实现基本的Redis操作方法：

- 字符串操作：`setString`, `getString`, `deleteKey`
- 列表操作：`addToListLeft`, `addToListRight`, `getList`
- 哈希操作：`putHash`, `getHash`, `getHashAll`

### 5. 控制器层实现

创建[RedisController.java](file:///Users/zhangyekai/IdeaProjects/learn/redis/springboot_redis/src/main/java/com/zyk_test/springboot_redis/controller/RedisController.java)提供REST API接口：

- 字符串操作：`POST /redis/string`, `GET /redis/string/{key}`
- 列表操作：`POST /redis/list/left`, `POST /redis/list/right`, `GET /redis/list/{key}`
- 哈希操作：`POST /redis/hash`, `GET /redis/hash/{key}/{hashKey}`, `GET /redis/hash/{key}`
- 通用操作：`DELETE /redis/key/{key}`, `GET /redis/key/{key}`

## 运行项目

### 1. 启动Redis服务器

确保Redis服务器正在运行。如果使用Docker，可以执行以下命令：

```bash
cd ../docker
docker-compose up -d
```

### 2. 编译和运行项目

```bash
mvn clean compile
mvn spring-boot:run
```

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/zyk_test/springboot_redis/
│   │       ├── SpringbootRedisApplication.java  # 主应用类
│   │       ├── config/                          # 配置类
│   │       │   └── RedisConfig.java
│   │       ├── controller/                      # 控制器
│   │       │   └── RedisController.java
│   │       └── service/                         # 服务类
│   │           └── RedisService.java
│   └── resources/
│       └── application.properties               # 应用配置
└── test/                                        # 测试代码
```

## 使用说明

项目启动后，可以通过以下REST API进行测试：

- 设置字符串值: `POST /redis/string?key=mykey&value=myvalue`
- 获取字符串值: `GET /redis/string/mykey`
- 设置字符串值并指定过期时间: `POST /redis/string/timeout?key=mykey&value=myvalue&timeout=60`
- 添加到列表左侧: `POST /redis/list/left?key=mylist&value=myvalue`
- 添加到列表右侧: `POST /redis/list/right?key=mylist&value=myvalue`
- 获取列表: `GET /redis/list/mylist`
- 从列表左侧弹出元素: `POST /redis/list/pop/left/mylist`
- 从列表右侧弹出元素: `POST /redis/list/pop/right/mylist`
- 设置哈希字段: `POST /redis/hash?key=myhash&hashKey=myfield&value=myvalue`
- 获取哈希字段: `GET /redis/hash/myhash/myfield`
- 获取所有哈希字段: `GET /redis/hash/myhash`
- 删除哈希字段: `DELETE /redis/hash/myhash?hashKeys=myfield`
- 删除键: `DELETE /redis/key/mykey`
- 检查键是否存在: `GET /redis/key/mykey`

## Redis 存储优化建议

在实际应用中，使用 Redis 存储对象时可能会遇到对象占用空间比实际数据大的问题。这是因为序列化过程中会包含额外的元数据信息。以下是几种优化方案：

### 1. 使用更高效的序列化方式

当前项目使用的是 [GenericJackson2JsonRedisSerializer](file:///Users/zhangyekai/IdeaProjects/learn/redis/springboot_redis/src/main/java/com/zyk_test/springboot_redis/config/RedisConfig.java#L3-L13)，它会存储类的类型信息以支持反序列化。我们可以使用更轻量级的序列化方式：

#### 方案一：使用 StringRedisSerializer 存储纯 JSON 字符串

```java
// 在 RedisConfig.java 中添加新的 RedisTemplate 配置
@Bean
public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new StringRedisSerializer());
    template.afterPropertiesSet();
    return template;
}
```

#### 方案二：使用 Hash 结构存储对象字段

将对象的每个字段存储为 Hash 的一个字段，而不是将整个对象序列化为一个字符串：

```java
// 在 RedisService.java 中添加方法
public void setObjectAsHash(String key, Object obj) {
    try {
        // 使用反射获取对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> fieldMap = new HashMap<>();
        
        for (Field field : fields) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(obj));
        }
        
        // 将字段存储为 Hash
        redisTemplate.opsForHash().putAll(key, fieldMap);
    } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to access object fields", e);
    }
}

public Map<Object, Object> getObjectAsHash(String key) {
    return redisTemplate.opsForHash().entries(key);
}
```

### 2. 实施建议

1. **分析存储数据**：首先分析应用中存储的对象类型和大小，确定哪些对象占用了过多空间。

2. **选择合适的方案**：
   - 对于简单的键值对，使用 StringRedisSerializer
   - 对于复杂对象，使用 Hash 结构存储
   - 对于大型文本数据，考虑压缩存储

3. **性能测试**：在实施任何优化方案前，进行性能测试以确保改进确实有效。

4. **监控空间使用**：使用 Redis 的 `INFO memory` 命令监控内存使用情况，确保优化措施有效。