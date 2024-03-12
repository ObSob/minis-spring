package org.minispring;

import org.minispring.core.bean.factory.config.context.ApplicationContext;
import org.minispring.core.bean.factory.config.context.ClassPathXmlApplicationContext;
import org.minispring.app.service.HelloService;
import org.minispring.app.service.HelloServiceImpl;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        HelloService service = (HelloServiceImpl) context.getBean(HelloServiceImpl.class.getName());
        service.greet("wbh");

    }

}