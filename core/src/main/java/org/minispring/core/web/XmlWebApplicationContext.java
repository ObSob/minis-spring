package org.minispring.core.web;

import jakarta.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.minispring.core.bean.factory.config.context.ClassPathXmlApplicationContext;

@Setter
@Getter
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {
    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

}