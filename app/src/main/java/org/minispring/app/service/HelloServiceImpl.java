package org.minispring.app.service;

import org.minispring.app.dao.UserDao;
import org.minispring.core.bean.annotation.Autowired;

//@Bean
public class HelloServiceImpl implements HelloService {

    @Autowired
    private UserDao userDao;

    @Override
    public void greet(String userName) {
//        userDao.saveUser(userName);
        System.out.println("hello " + userName);
    }
}
