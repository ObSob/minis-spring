package org.minispring.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.minispring.app.entity.User;
import org.minispring.core.bean.factory.config.context.ApplicationContext;
import org.minispring.core.bean.factory.config.context.ClassPathXmlApplicationContext;
import org.minispring.core.jdbc.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        UserJdbcImpl jdbcTemplate = (UserJdbcImpl) context.getBean("jdbcTemplate");
        UserService userService = new UserService(jdbcTemplate);
        DefaultSqlSessionFactory sessionFactory = (DefaultSqlSessionFactory) context.getBean("sqlSessionFactory");
        sessionFactory.setJdbcTemplate(jdbcTemplate);
        userService.setSqlSessionFactory(sessionFactory);

        int testUserId = 1; // 假设你想测试的用户ID为1
        List<User> users = userService.getUser(testUserId);
        for (User user : users) {
            if (user != null) {
                log.info("用户ID: {}", user.getId());
                log.info("用户名: {}", user.getName());
                log.info("生日: {}", user.getBirthday());
            } else {
                log.info("未找到用户ID为 {} 的用户信息。", testUserId);
            }
        }

        userService.getUserInfo(1);
    }
}
