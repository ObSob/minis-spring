package org.minispring.web;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.jsp.JettyJspServlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        // 创建Jetty服务器实例，监听8080端口
        Server server = new Server(8080);

        // 创建WebAppContext
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/"); // 设置上下文路径

        // 使用类加载器来获取编译后的资源路径
        ClassLoader cl = Main.class.getClassLoader();
        URL f = cl.getResource("webapps");
        Objects.requireNonNull(f, "webapps目录未找到");
        webapp.setResourceBase(f.toExternalForm()); // 设置资源基础路径

        // 设置Web描述符路径
        URL webXmlUrl = cl.getResource("webapps/WEB-INF/web.xml");
        Objects.requireNonNull(webXmlUrl, "web.xml文件未找到");
        webapp.setDescriptor(webXmlUrl.toExternalForm()); // 设置Web描述符路径

        // 将WebAppContext设置为服务器的处理器
        server.setHandler(webapp);

        enableEmbeddedJspSupport(webapp);

        // 启动服务器
        server.start();
        server.join();
    }

    private static void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws Exception {
        // Establish Scratch directory for the servlet context (used by JSP compilation)
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);

        // Set Classloader of Context to be sane (needed for JSTL)
        // JSP requires a non-System classloader, this simply wraps the
        // embedded System classloader in a way that makes it suitable
        // for JSP to use
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], Main.class.getClassLoader());
        servletContextHandler.setClassLoader(jspClassLoader);

        // Manually call JettyJasperInitializer on context startup

        EmbeddedJspStarter o = new EmbeddedJspStarter(servletContextHandler);
//        o.start();
        servletContextHandler.addBean(o);

        // Create / Register JSP Servlet (must be named "jsp" per spec)
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("scratchdir", scratchDir.toString());
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        servletContextHandler.addServlet(holderJsp, "*.jsp");

        servletContextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
    }
}
