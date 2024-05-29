package org.minispring.app.service;

import lombok.extern.slf4j.Slf4j;
import org.minispring.app.dao.UserDao;
import org.minispring.core.bean.annotation.Autowired;

//@Bean
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Autowired
    private UserDao userDao;

    @Override
    public void greet(String userName) {
//        userDao.saveUser(userName);
        log.info("hello {}", userName);
    }
}
