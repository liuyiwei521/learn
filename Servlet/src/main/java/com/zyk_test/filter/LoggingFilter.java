package com.zyk_test.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 请求日志过滤器示例
 * 记录所有请求的基本信息和处理时间
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "LoggingFilter")
public class LoggingFilter implements Filter {
    
    // 创建日志记录器
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());
    
    /**
     * 初始化过滤器
     * 在过滤器实例化后调用一次
     * @param filterConfig 过滤器配置对象
     * @throws ServletException 如果初始化过程中发生错误
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("LoggingFilter 初始化开始");
        try {
            // 这里可以添加初始化逻辑
            logger.info("LoggingFilter 初始化完成");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "LoggingFilter 初始化失败", e);
            throw new ServletException("过滤器初始化失败", e);
        }
    }
    
    /**
     * 执行过滤逻辑
     * 每次请求/响应通过过滤器时调用
     * @param request 请求对象
     * @param response 响应对象
     * @param chain 过滤器链对象
     * @throws IOException 如果输入/输出操作过程中发生错误
     * @throws ServletException 如果Servlet处理过程中发生错误
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        
        // 初始化请求和响应信息变量
        String method = "UNKNOWN";
        String uri = "UNKNOWN";
        String queryString = "UNKNOWN";
        String remoteAddr = "UNKNOWN";
        String userAgent = "UNKNOWN";
        
        try {
            // 将ServletRequest和ServletResponse转换为HttpServlet类型以便获取更多信息
            if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                
                // 获取请求信息
                method = httpRequest.getMethod();
                uri = httpRequest.getRequestURI();
                queryString = httpRequest.getQueryString();
                remoteAddr = httpRequest.getRemoteAddr();
                userAgent = httpRequest.getHeader("User-Agent");
            }
            
            // 输出请求信息到日志
            logger.info("==========================================");
            logger.info("请求开始时间: " + new Date(startTime));
            logger.info("请求方法: " + method);
            logger.info("请求URI: " + uri);
            logger.info("查询参数: " + queryString);
            logger.info("客户端IP: " + remoteAddr);
            logger.info("User-Agent: " + userAgent);
            
            // 继续执行过滤器链，将请求传递给下一个过滤器或目标Servlet
            chain.doFilter(request, response);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "过滤器处理过程中发生IO异常", e);
            // 记录处理时间
            long endTime = System.currentTimeMillis();
            logger.info("请求结束时间: " + new Date(endTime));
            logger.info("处理时间: " + (endTime - startTime) + "ms");
            logger.info("==========================================");
            throw e;
        } catch (ServletException e) {
            logger.log(Level.WARNING, "过滤器处理过程中发生Servlet异常", e);
            // 记录处理时间
            long endTime = System.currentTimeMillis();
            logger.info("请求结束时间: " + new Date(endTime));
            logger.info("处理时间: " + (endTime - startTime) + "ms");
            logger.info("==========================================");
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "过滤器处理过程中发生未预期的异常", e);
            // 记录处理时间
            long endTime = System.currentTimeMillis();
            logger.info("请求结束时间: " + new Date(endTime));
            logger.info("处理时间: " + (endTime - startTime) + "ms");
            logger.info("==========================================");
            throw new ServletException("过滤器处理过程中发生未预期的异常", e);
        } finally {
            try {
                // 记录请求结束时间和处理时间
                long endTime = System.currentTimeMillis();
                long processTime = endTime - startTime;
                
                // 尝试获取响应状态码
                int status = -1;
                if (response instanceof HttpServletResponse) {
                    status = ((HttpServletResponse) response).getStatus();
                }
                
                // 输出响应信息到日志
                logger.info("响应状态码: " + status);
                logger.info("处理时间: " + processTime + "ms");
                logger.info("请求结束时间: " + new Date(endTime));
                logger.info("==========================================");
            } catch (Exception e) {
                logger.log(Level.WARNING, "记录响应信息时发生错误", e);
            }
        }
    }
    
    /**
     * 销毁过滤器
     * 在过滤器被销毁前调用一次，用于释放资源
     */
    @Override
    public void destroy() {
        logger.info("LoggingFilter 销毁开始");
        try {
            // 这里可以添加清理资源的逻辑
            logger.info("LoggingFilter 销毁完成");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "LoggingFilter 销毁过程中发生错误", e);
        }
    }
}