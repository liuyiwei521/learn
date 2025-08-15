# 使用 Docker 部署 Tomcat 全流程指南

本指南将详细介绍如何使用 Docker 部署 Tomcat 服务器，并将我们的 Servlet 应用部署到其中。

## 目录
1. [环境准备](#环境准备)
2. [获取 Tomcat 镜像](#获取-tomcat-镜像)
3. [构建应用 WAR 包](#构建应用-war-包)
4. [部署应用到 Tomcat](#部署应用到-tomcat)
5. [运行 Tomcat 容器](#运行-tomcat-容器)
6. [验证部署](#验证部署)
7. [常用管理命令](#常用管理命令)
8. [调试方法](#调试方法)
9. [常见问题及解决方案](#常见问题及解决方案)

## 环境准备

确保已安装以下软件：
- Docker (版本 18.06 或更高)
- Maven (用于构建项目)

检查 Docker 是否正常工作：
```bash
docker --version
docker-compose --version  # 如果使用 Docker Compose
```

## 获取 Tomcat 镜像

### 1. 搜索可用的 Tomcat 镜像
```bash
docker search tomcat
```

### 2. 拉取官方 Tomcat 镜像
推荐使用官方 Tomcat 镜像，可以选择合适的版本：
```bash
# 拉取最新的 Tomcat 镜像
docker pull tomcat

# 或者拉取指定版本的 Tomcat 镜像
docker pull tomcat:9.0-jdk11-openjdk
```

### 3. 验证镜像是否成功下载
```bash
docker images | grep tomcat
```

## 构建应用 WAR 包

在我们的 Servlet 项目目录中执行以下命令：

### 1. 清理并编译项目
```bash
cd F:\learn\Servlet
mvn clean compile
```

### 2. 打包为 WAR 文件
```bash
mvn package
```

这将在 `target` 目录下生成一个 WAR 文件，例如 `Servlet-1.0-SNAPSHOT.war`。

### 3. 重命名 WAR 文件（可选）
为了方便部署，可以将 WAR 文件重命名为更简单的名称：
```bash
cp target/Servlet-1.0-SNAPSHOT.war target/Servlet.war
```

## 部署应用到 Tomcat

### 方法一：使用卷挂载方式部署

#### 1. 创建部署目录
```bash
mkdir -p ~/tomcat/webapps
```

#### 2. 复制 WAR 文件到部署目录
```bash
cp target/Servlet.war ~/tomcat/webapps/
```

### 方法二：构建自定义镜像（推荐）

#### 1. 创建 Dockerfile
在项目根目录创建 [Dockerfile](file:///F:/learn/Servlet/Dockerfile) 文件：
```dockerfile
# 使用官方 Tomcat 镜像作为基础镜像
FROM tomcat:9.0-jdk11-openjdk

# 删除默认的 webapps 内容
RUN rm -rf /usr/local/tomcat/webapps/*

# 将我们的应用 WAR 包复制到 Tomcat webapps 目录
COPY target/Servlet-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# 暴露 Tomcat 默认端口
EXPOSE 8080

# 启动 Tomcat
CMD ["catalina.sh", "run"]
```

#### 2. 构建自定义镜像
```bash
docker build -t my-servlet-app .
```

## 运行 Tomcat 容器

### 方法一：使用卷挂载方式运行

```bash
docker run -d \
  --name my-tomcat \
  -p 8080:8080 \
  -v ~/tomcat/webapps:/usr/local/tomcat/webapps \
  tomcat:9.0-jdk11-openjdk
```

### 方法二：使用自定义镜像运行（推荐）

```bash
docker run -d \
  --name my-servlet-app \
  -p 8080:8080 \
  my-servlet-app
```

### 方法三：使用 Docker Compose（可选）

创建 [docker-compose.yml](file:///F:/learn/redis-cluster/docker-compose.yml) 文件：
```yaml
version: '3.8'

services:
  tomcat:
    build: .
    container_name: my-servlet-app
    ports:
      - "8080:8080"
    restart: unless-stopped
```

然后运行：
```bash
docker-compose up -d
```

## 验证部署

### 1. 检查容器运行状态
```bash
docker ps
```

### 2. 查看容器日志
```bash
docker logs my-servlet-app
```

### 3. 访问应用
打开浏览器并访问以下地址：
- 主页: http://localhost:8080/
- Servlet: http://localhost:8080/hello

### 4. 验证应用功能
检查以下内容：
- 页面是否正常显示
- 时间是否正确更新
- GET/POST 请求是否正常处理

## 常用管理命令

### 容器管理
```bash
# 停止容器
docker stop my-servlet-app

# 启动容器
docker start my-servlet-app

# 重启容器
docker restart my-servlet-app

# 删除容器
docker rm my-servlet-app
```

### 日志查看
```bash
# 查看实时日志
docker logs -f my-servlet-app

# 查看最近的日志
docker logs --tail 50 my-servlet-app
```

### 进入容器
```bash
# 进入运行中的容器
docker exec -it my-servlet-app /bin/bash
```

### 镜像管理
```bash
# 查看镜像
docker images

# 删除镜像
docker rmi my-servlet-app
```

## 调试方法

### 1. 查看应用日志

#### 查看容器日志
```bash
# 查看实时日志
docker logs -f my-servlet-app

# 查看最近的50行日志
docker logs --tail 50 my-servlet-app
```

#### 查看Tomcat内部日志
```bash
# 进入容器查看详细日志
docker exec -it my-servlet-app bash
cd /usr/local/tomcat/logs
cat catalina.out
cat localhost.*.log
```

### 2. 在代码中添加调试信息

在Servlet中添加详细的日志输出：
```java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    // 添加调试信息
    System.out.println("处理 GET 请求");
    logRequestInfo(request); // 记录请求信息
    
    // ... 处理逻辑 ...
}

private void logRequestInfo(HttpServletRequest request) {
    System.out.println("=== 请求信息 ===");
    System.out.println("请求方法: " + request.getMethod());
    System.out.println("请求URI: " + request.getRequestURI());
    // ... 更多请求信息 ...
}
```

### 3. 使用调试脚本

项目提供了调试脚本:
- Linux/Mac: [debug.sh](file:///F:/learn/Servlet/debug.sh)
- Windows: [debug.cmd](file:///F:/learn/Servlet/debug.cmd)

运行脚本可以快速查看容器状态和日志。

### 4. 进入容器调试

```bash
# 进入运行中的容器
docker exec -it my-servlet-app /bin/bash

# 在容器中可以检查以下内容:
# 1. 检查webapps目录
ls -la /usr/local/tomcat/webapps/

# 2. 查看日志文件
tail -f /usr/local/tomcat/logs/catalina.out

# 3. 检查环境变量
env
```

### 5. 网络调试

```bash
# 检查端口是否在监听
netstat -an | findstr 8080  # Windows
# 或
netstat -an | grep 8080     # Linux/Mac

# 测试应用连接
curl -v http://localhost:8080/
```

## 常见问题及解决方案

### 问题1: 应用无法访问
检查:
1. 容器是否正常运行: `docker ps`
2. 端口是否正确映射: `docker port my-servlet-app`
3. 应用是否正确部署: 进入容器检查 `/usr/local/tomcat/webapps/` 目录
4. 查看Tomcat日志: `docker logs my-servlet-app`

### 问题2: 端口冲突
```bash
# 更改端口映射
docker run -d --name my-servlet-app -p 8081:8080 my-servlet-app
```

### 问题3: 构建失败
```bash
# 检查Maven构建
mvn clean package -X  # -X显示详细错误信息
```

### 问题4: 应用启动缓慢
检查Tomcat日志中的启动信息，可能需要增加JVM内存：
```dockerfile
# 在Dockerfile中添加
ENV JAVA_OPTS="-Xmx512m -Xms256m"
```

### 问题5: 404错误
1. 检查WAR包是否正确部署到webapps目录
2. 检查应用上下文路径是否正确
3. 查看Tomcat日志中是否有部署错误信息

## 故障排除

### 常见问题及解决方案

#### 1. 端口冲突
如果 8080 端口已被占用，可以更改端口映射：
```bash
docker run -d \
  --name my-servlet-app \
  -p 8081:8080 \
  my-servlet-app
```

#### 2. 应用无法访问
检查以下几点：
- 容器是否正常运行：`docker ps`
- 端口映射是否正确
- 应用是否正确部署到 webapps 目录
- 查看容器日志：`docker logs my-servlet-app`

#### 3. 权限问题
在 Linux 系统上，可能需要调整文件权限：
```bash
chmod 755 target/*.war
```

## 最佳实践

1. **使用特定版本标签**：避免使用 latest 标签，使用特定版本以确保一致性
2. **优化镜像大小**：使用轻量级基础镜像
3. **安全配置**：不要在容器中以 root 用户运行应用
4. **环境变量**：使用环境变量配置应用参数
5. **健康检查**：添加健康检查以监控应用状态
6. **日志管理**：配置适当的日志记录策略

## 总结

通过以上步骤，我们已经成功使用 Docker 部署了 Tomcat 服务器，并将我们的 Servlet 应用运行在其中。Docker 提供了一种轻量级、可移植的部署方式，使得应用可以在任何支持 Docker 的环境中运行，而无需担心环境配置问题。

调试是开发过程中不可或缺的一部分。通过日志查看、进入容器、使用调试脚本等方法，我们可以快速定位和解决问题，确保应用正常运行。