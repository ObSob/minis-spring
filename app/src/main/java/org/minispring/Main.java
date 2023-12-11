package org.minispring;

import org.minispring.core.bean.config.context.ApplicationContext;
import org.minispring.core.bean.config.context.ClassPathXmlApplicationContext;
import org.minispring.service.HelloService;
import org.minispring.service.HelloServiceImpl;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        HelloService service = (HelloServiceImpl) context.getBean(HelloServiceImpl.class.getName());
        service.greet();
    }
}