package org.minispring.app.dao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDaoImpl implements UserDao {
    @Override
    public int saveUser(String userName) {
        log.info("添加{}", userName);
        return 0;
    }
}
