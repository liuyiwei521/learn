package com.zyk_test;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Enumeration;
ao import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 简单的HelloWorld Servlet示例
 * 演示了Servlet的基本结构和生命周期方法
 */
public class HelloWorldServlet extends HttpServlet {
    
    // 创建日志记录器
    private static final Logger logger = Logger.getLogger(HelloWorldServlet.class.getName());
    
    /**
     * Servlet初始化方法
     * 在Servlet实例创建后调用一次，用于执行一次性设置
     * @throws ServletException 如果初始化过程中发生错误
     */
    @Override
    public void init() throws ServletException {
        logger.info("HelloWorldServlet 初始化开始");
        try {
            super.init();
            logger.info("HelloWorldServlet 初始化完成");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "HelloWorldServlet 初始化失败", e);
            throw new ServletException("Servlet初始化失败", e);
        }
    
    /**
     * 处理HTTP GET请求
     * @param request HTTP请求对象，包含客户端发送的请求信息
     * @param response HTTP响应对象，用于向客户端发送响应
     * @throws ServletException 如果Servlet处理过程中发生错误
     * @throws IOException 如果输入/输出操作过程中发生错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 添加调试信息
        logger.info("处理 GET 请求");
        logRequestInfo(request);
        
        // 设置响应内容类型和字符编码
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = null;
        try {
            // 获取输出流，用于向客户端发送响应内容
            out = response.getWriter();
            
            // 输出HTML内容
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>欢迎来到 Servlet 世界!</h1>");
            out.println("<p>这是一个基本的 Servlet 示例。</p>");
            out.println("<p>当前时间: " + new java.util.Date() + "</p>");
            
            // 模拟可能的业务逻辑异常
            String param = request.getParameter("error");
            if ("true".equals(param)) {
                throw new ServletException("模拟业务逻辑异常");
            }
            
            out.println("</body>");
            out.println("</html>");
            
            logger.info("成功生成响应内容");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO异常，无法写入响应内容", e);
            // 设置错误响应
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "服务器内部错误: " + e.getMessage());
            throw e;
        } catch (ServletException e) {
            logger.log(Level.WARNING, "Servlet异常", e);
            // 设置错误响应
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                              "请求处理错误: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "处理请求时发生未预期的错误", e);
            // 设置错误响应
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "服务器内部错误: " + e.getMessage());
            throw new ServletException("处理请求时发生未预期的错误", e);
        } finally {
            // 关闭输出流以释放资源
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "关闭输出流时发生错误", e);
                }
            }
        }
    }
    
    /**
     * 处理HTTP POST请求
     * @param request HTTP请求对象，包含客户端发送的请求信息
     * @param response HTTP响应对象，用于向客户端发送响应
     * @throws ServletException 如果Servlet处理过程中发生错误
     * @throws IOException 如果输入/输出操作过程中发生错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("处理 POST 请求");
        logRequestInfo(request);
        try {
            // 处理POST请求，这里简单地调用doGet方法
            doGet(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "处理POST请求时发生错误", e);
            throw new ServletException("处理POST请求时发生错误", e);
        }
    }
    
    /**
     * Servlet销毁方法
     * 在Servlet被销毁前调用一次，用于释放资源
     */
    @Override
    public void destroy() {
        logger.info("HelloWorldServlet 销毁开始");
        try {
            super.destroy();
            logger.info("HelloWorldServlet 销毁完成");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "HelloWorldServlet 销毁过程中发生错误", e);
        }
    }
    
    /**
     * 记录请求信息用于调试
     * @param request HTTP请求对象
     */
    private void logRequestInfo(HttpServletRequest request) {
        try {
            logger.info("=== 请求信息 ===");
            logger.info("请求方法: " + request.getMethod());
            logger.info("请求URI: " + request.getRequestURI());
            logger.info("查询字符串: " + request.getQueryString());
            logger.info("远程地址: " + request.getRemoteAddr());
            logger.info("用户代理: " + request.getHeader("User-Agent"));
            
            // 打印所有请求头
            logger.info("请求头信息:");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logger.info("  " + headerName + ": " + request.getHeader(headerName));
            }
            
            logger.info("===============");
        } catch (Exception e) {
            logger.log(Level.WARNING, "记录请求信息时发生错误", e);
        }
    }
}