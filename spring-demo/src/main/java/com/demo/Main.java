package com.demo;

import com.demo.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        System.out.println(context.getApplicationName());

        HelloService helloService = (HelloService) context.getBean("helloService");
        helloService.hello();
    }
}
