@echo off
setlocal enabledelayedexpansion
chcp 65001
:: 调试模式：逐行执行并暂停
echo ==============================================
echo 开始执行Git自动提交推送脚本（调试模式）
echo 每步操作后按任意键继续，按Ctrl+C可终止脚本
echo ==============================================
pause

:: 配置区域
echo.
echo 【步骤1/9】设置配置参数
set "MAIN_BRANCH=main"
             :: 主分支名称
set "COMMIT_MSG=Auto commit by script"
  :: 默认提交信息
echo. - 主分支: %MAIN_BRANCH%
echo. - 提交信息: %COMMIT_MSG%
pause

:: 获取当前目录作为项目路径
echo.
echo 【步骤2/9】确认项目路径（脚本所在目录）
set "PROJECT_PATH=%cd%"
echo. - 检测到的项目路径: %PROJECT_PATH%
echo. - 请确认此路径是learn文件夹根目录
pause

:: 检查是否为Git仓库
echo.
echo 【步骤3/9】检查是否为Git仓库
if not exist .git (
    echo 错误：当前目录不是Git仓库（未找到.git文件夹）
    pause
    exit /b 1
) else (
    echo. - 确认是Git仓库
)
pause

:: 拉取最新代码
echo.
echo 【步骤4/9】拉取主分支最新代码
echo. - 执行命令: git pull origin %MAIN_BRANCH%
git pull origin %MAIN_BRANCH%

:: 显示拉取结果
echo.
echo. - git pull执行结果代码: %errorlevel%（0=成功，非0=失败/冲突）
pause

:: 根据拉取结果分支处理
if %errorlevel% equ 0 (
    echo.
    echo 【步骤5/9】拉取成功，准备提交更改
    echo. - 执行命令: git add .
    git add .
    echo. - 添加完成，检查是否有可提交内容
    pause

    :: 检查是否有需要提交的内容
    echo.
    echo 【步骤6/9】检查暂存区是否有更改
    git diff --quiet --cached
    set "HAS_CHANGES=%errorlevel%"
    echo. - 检查结果代码: %HAS_CHANGES%（0=无更改，1=有更改）
    if "%HAS_CHANGES%"=="0" (
        echo. - 没有需要提交的更改，操作结束
        pause
        goto continue_processing
    )
    pause

    :: 提交更改
    echo.
    echo 【步骤7/9】提交更改到本地仓库
    echo. - 执行命令: git commit -m "%COMMIT_MSG% - %date% %time%"
    git commit -m "%COMMIT_MSG% - %date% %time%"
    echo. - 提交结果代码: %errorlevel%（0=成功）
    if %errorlevel% neq 0 (
        echo 错误：提交失败
        pause
        exit /b 1
    )
    pause

    :: 推送更改
    echo.
    echo 【步骤8/9】推送更改到远程主分支
    echo. - 执行命令: git push origin %MAIN_BRANCH%
    git push origin %MAIN_BRANCH%
    echo. - 推送结果代码: %errorlevel%（0=成功）
    if %errorlevel% equ 0 (
        echo. - 推送成功完成
    ) else (
        echo 错误：推送失败，可能因为包含敏感信息或网络问题
        echo 请检查:
        echo 1. 是否在提交中包含了个人访问令牌等敏感信息
        echo 2. 网络连接是否正常
        echo 3. 是否有推送权限
        pause
        exit /b 1
    )
    pause

) else (
    echo.
    echo 【步骤5/9】拉取时检测到冲突，准备创建新分支
    pause

    :: 生成基于当前时间的分支名
    echo.
    echo 【步骤6/9】生成新分支名称（基于当前时间）
    for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
    :: 检查是否成功获取时间
    if "!dt!"=="" (
        :: 如果wmic方法失败，使用备用方法
        set "BRANCH_NAME=auto-conflict-%date:~0,4%%date:~5,2%%date:~8,2%-%time:~0,2%%time:~3,2%%time:~6,2%"
        :: 清理时间字符串中的空格和特殊字符
        set "BRANCH_NAME=!BRANCH_NAME: =0!"
        set "BRANCH_NAME=!BRANCH_NAME:/=!"
        set "BRANCH_NAME=!BRANCH_NAME:.=!"
        set "BRANCH_NAME=!BRANCH_NAME::-=!"
    ) else (
        set "BRANCH_NAME=auto-conflict-!dt:~0,8!-!dt:~8,6!"
    )
    echo. - 生成的分支名: !BRANCH_NAME!
    pause

    :: 创建并切换到新分支
    echo.
    echo 【步骤7/9】创建并切换到新分支
    echo. - 执行命令: git checkout -b !BRANCH_NAME!
    git checkout -b !BRANCH_NAME!
    echo. - 执行结果代码: %errorlevel%（0=成功）
    if %errorlevel% neq 0 (
        echo 错误：创建新分支失败
        pause
        exit /b 1
    )
    pause

    :: 提交更改到新分支
    echo.
    echo 【步骤8/9】提交更改到新分支
    echo. - 执行命令: git add .
    git add .

    git commit -m "Conflict branch: !BRANCH_NAME! - %date% %time%"
    echo. - 提交结果代码: %errorlevel%（0=成功）
    if %errorlevel% neq 0 (
        echo 错误：提交到新分支失败
        pause
        exit /b 1
    )
    pause

    :: 推送新分支
    echo.
    echo 【步骤9/9】推送新分支到远程
    echo. - 执行命令: git push -u origin !BRANCH_NAME!
    git push -u origin !BRANCH_NAME!
    echo. - 推送结果代码: %errorlevel%（0=成功）
    if %errorlevel% neq 0 (
        echo 错误：推送新分支失败
        echo 可能原因:
        echo 1. 包含敏感信息（如个人访问令牌）
        echo 2. 网络连接问题
        echo 3. 推送权限问题
        pause
        exit /b 1
    )

    :: 切换回主分支
    echo.
    echo 【额外步骤】切换回主分支
    git checkout %MAIN_BRANCH%
    echo. - 切换结果代码: %errorlevel%（0=成功）
    pause
)

:continue_processing
echo.
echo ==============================================
echo 脚本执行完毕
echo ==============================================
pause

endlocal