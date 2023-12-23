package org.minispring.app.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public int saveUser(String userName) {
        System.out.println("添加" + userName);
        return 0;
    }
}
