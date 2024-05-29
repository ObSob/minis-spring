package org.minispring.core.web;

import jakarta.servlet.ServletContext;
import org.minispring.core.bean.factory.config.context.ApplicationContext;

public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName();

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);
}
