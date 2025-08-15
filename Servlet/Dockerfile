# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:21-jdk

# 设置维护者信息
LABEL maintainer="张叶凯"

# 设置工作目录
WORKDIR /app

# 将我们的应用 JAR 包复制到容器中
COPY target/Servlet-1.0-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]