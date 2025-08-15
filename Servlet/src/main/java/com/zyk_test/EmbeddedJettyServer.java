package com.zyk_test;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * 嵌入式Jetty服务器示例
 * 演示如何使用嵌入式Jetty服务器运行Servlet应用
 */
public class EmbeddedJettyServer {
    /**
     * 程序入口点
     * 启动嵌入式Jetty服务器并注册Servlet
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Server server = null;
        try {
            Logger logger = Logger.getLogger(EmbeddedJettyServer.class.getName());
            logger.info("开始启动Jetty服务器");
            
            // 创建Jetty服务器实例，监听8080端口
            server = new Server(8080);
            
            // 创建ServletContextHandler用于处理Servlet请求
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            
            // 注册HelloWorldServlet到/hello路径
            context.addServlet(new ServletHolder(new HelloWorldServlet()), "/hello");
            
            // 添加默认页面Servlet
            context.addServlet(new ServletHolder(new DefaultServlet()), "/");
            
            // 启动服务器
            server.start();
            logger.info("Jetty服务器启动成功，访问地址: http://localhost:8080/");
            System.out.println("Jetty服务器已启动，访问地址: http://localhost:8080/");
            System.out.println("HelloWorldServlet访问地址: http://localhost:8080/hello");
            
            // 等待服务器停止
            server.join();
        } catch (Exception e) {
            Logger logger = Logger.getLogger(EmbeddedJettyServer.class.getName());
            logger.log(Level.SEVERE, "启动Jetty服务器时发生错误", e);
            System.err.println("启动Jetty服务器时发生错误: " + e.getMessage());
            e.printStackTrace();
            
            // 尝试停止服务器
            if (server != null) {
                try {
                    server.stop();
                } catch (Exception stopException) {
                    logger.log(Level.SEVERE, "停止服务器时发生错误", stopException);
                }
            }
            
            // 退出程序
            System.exit(1);
    }
    
    /**
     * 默认页面Servlet
     * 处理根路径(/)的请求
     */
    public static class DefaultServlet extends HttpServlet {
        
        // 创建日志记录器
        private static final Logger logger = Logger.getLogger(DefaultServlet.class.getName());
        
        /**
         * 处理HTTP GET请求
         * @param request HTTP请求对象
         * @param response HTTP响应对象
         * @throws ServletException 如果Servlet处理过程中发生错误
         * @throws IOException 如果输入/输出操作过程中发生错误
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            
            logger.info("处理默认页面请求");
            
            // 设置响应内容类型和字符编码
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = null;
            
            try {
                out = response.getWriter();
                
                // 输出HTML欢迎页面
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Embedded Jetty Server</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>欢迎使用嵌入式Jetty服务器!</h1>");
                out.println("<p>这是一个使用JAR包运行的Servlet应用。</p>");
                out.println("<h2>可用的Servlet:</h2>");
                out.println("<ul>");
                out.println("<li><a href='/hello'>HelloWorldServlet</a> - 基本的 Servlet 示例</li>");
                out.println("</ul>");
                out.println("<h2>测试异常处理:</h2>");
                out.println("<ul>");
                out.println("<li><a href='/hello?error=true'>触发Servlet异常</a></li>");
                out.println("</ul>");
                out.println("</body>");
                out.println("</html>");
                
                logger.info("成功生成默认页面响应");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IO异常，无法写入响应内容", e);
                throw e;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "处理请求时发生未预期的错误", e);
                throw new ServletException("处理请求时发生未预期的错误", e);
            } finally {
                // 关闭输出流
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "关闭输出流时发生错误", e);
                    }
                }
        }
    }
}