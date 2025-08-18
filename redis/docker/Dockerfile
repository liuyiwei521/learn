# 使用官方 Redis 镜像作为基础镜像
FROM redis:7-alpine

# 设置维护者信息
LABEL maintainer="张叶凯"

# 将本地的 redis.conf 复制到容器中
COPY redis.conf /usr/local/etc/redis/redis.conf

# 设置工作目录
WORKDIR /data

# 暴露 Redis 端口
EXPOSE 6379

# 设置容器启动时执行的命令
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]