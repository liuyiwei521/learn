@echo off
chcp 65001 >nul

echo === Servlet应用调试脚本 ===

REM 检查容器是否运行
echo 1. 检查容器状态...
docker ps | findstr my-servlet-app >nul
if %errorlevel% == 0 (
    echo 容器正在运行
) else (
    echo 容器未运行
)

REM 显示容器日志
echo 2. 显示最近的日志...
docker logs --tail 20 my-servlet-app

REM 显示网络信息
echo 3. 显示端口映射...
docker port my-servlet-app

REM 调试命令提示
echo 4. 调试命令:
echo    查看实时日志: docker logs -f my-servlet-app
echo    进入容器: docker exec -it my-servlet-app /bin/bash
echo    重建应用: mvn clean package && docker build -t my-servlet-app .
echo    重启容器: docker restart my-servlet-app

pause