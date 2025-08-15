package com.zyk_test.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 字符编码过滤器
 * 统一设置请求和响应的字符编码，防止中文乱码问题
 */
@WebFilter(
    urlPatterns = {"/*"},
    filterName = "CharacterEncodingFilter",
    initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8"),
        @WebInitParam(name = "forceEncoding", value = "true")
    }
)
public class CharacterEncodingFilter implements Filter {
    
    // 创建日志记录器
    private static final Logger logger = Logger.getLogger(CharacterEncodingFilter.class.getName());

    // 字符编码格式
    private String encoding;
    
    // 是否强制设置编码
    private boolean forceEncoding;
    
    /**
     * 初始化过滤器
     * 获取初始化参数并设置字符编码配置
     * @param filterConfig 过滤器配置对象
     * @throws ServletException 如果初始化过程中发生错误
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CharacterEncodingFilter 初始化开始");
        try {
            // 获取初始化参数
            encoding = filterConfig.getInitParameter("encoding");
            String forceEncodingParam = filterConfig.getInitParameter("forceEncoding");
            
            // 如果没有设置编码参数，则使用默认的UTF-8编码
            if (encoding == null || encoding.trim().isEmpty()) {
                encoding = "UTF-8";
                logger.info("未指定编码参数，使用默认编码: UTF-8");
            }
            
            // 解析强制编码参数，默认为false
            forceEncoding = Boolean.parseBoolean(forceEncodingParam);
            
            logger.info("CharacterEncodingFilter 初始化完成");
            logger.info("字符编码: " + encoding);
            logger.info("强制编码: " + forceEncoding);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "CharacterEncodingFilter 初始化失败", e);
            throw new ServletException("字符编码过滤器初始化失败", e);
        }
    }
    
    /**
     * 执行字符编码设置过滤逻辑
     * @param request 请求对象
     * @param response 响应对象
     * @param chain 过滤器链对象
     * @throws IOException 如果输入/输出操作过程中发生错误
     * @throws ServletException 如果Servlet处理过程中发生错误
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        try {
            // 设置请求字符编码
            // 如果强制编码为true，或者请求尚未设置字符编码，则设置字符编码
            if (forceEncoding || request.getCharacterEncoding() == null) {
                request.setCharacterEncoding(encoding);
                logger.fine("已设置请求字符编码为: " + encoding);
            }
            
            // 设置响应字符编码和内容类型
            response.setCharacterEncoding(encoding);
            response.setContentType("text/html;charset=" + encoding);
            logger.fine("已设置响应字符编码为: " + encoding);
            
            // 继续执行过滤器链
            chain.doFilter(request, response);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "设置字符编码时发生IO异常", e);
            throw e;
        } catch (ServletException e) {
            logger.log(Level.WARNING, "设置字符编码时发生Servlet异常", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "设置字符编码时发生未预期的异常", e);
            throw new ServletException("设置字符编码时发生未预期的异常", e);
        }
    }
    
    /**
     * 销毁过滤器
     * 在过滤器被销毁前调用一次，用于释放资源
     */
    @Override
    public void destroy() {
        logger.info("CharacterEncodingFilter 销毁开始");
        try {
            // 这里可以添加清理资源的逻辑
            encoding = null;
            logger.info("CharacterEncodingFilter 销毁完成");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "CharacterEncodingFilter 销毁过程中发生错误", e);
        }
    }
}