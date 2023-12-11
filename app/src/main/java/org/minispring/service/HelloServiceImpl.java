package org.minispring.service;

//@Bean
public class HelloServiceImpl implements HelloService {
    @Override
    public void greet() {
        System.out.println("hello");
    }
}
