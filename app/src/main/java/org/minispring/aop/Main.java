package org.minispring.aop;

import org.minispring.app.service.HelloService;
import org.minispring.app.service.HelloServiceImpl;
import org.minispring.core.bean.factory.config.context.ApplicationContext;
import org.minispring.core.bean.factory.config.context.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Action action = (Action) context.getBean("action");
        action.execute();

        Action autoProxyAction = (Action) context.getBean("autoProxyAction");
        autoProxyAction.execute();
    }
}
