package org.minispring.jdbc;

import lombok.Setter;
import org.minispring.app.entity.User;
import org.minispring.core.bean.annotation.Autowired;
import org.minispring.core.jdbc.DefaultSqlSessionFactory;
import org.minispring.core.jdbc.JdbcTemplate;
import org.minispring.core.jdbc.RowMapper;
import org.minispring.core.jdbc.SqlSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserService {

    //    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Setter
    private DefaultSqlSessionFactory sqlSessionFactory;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUser(int userid) {
        final String sql = "select id, name,birthday from user where id=?";
        return jdbcTemplate.query(sql, new Object[]{userid}, (rs, i) -> {
            User rtnUser = new User();
            rtnUser.setId(rs.getInt("id"));
            rtnUser.setName(rs.getString("name"));
            rtnUser.setBirthday(new Date(rs.getDate("birthday").getTime()));
            return rtnUser;
        });
    }

    public User getUserInfo(int userid) {
//        final String sql = "select id, name,birthday from users where id=?";
        String sqlid = "org.minispring.app.entity.User.getUserInfo";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (User) sqlSession.selectOne(sqlid, new Object[]{new Integer(userid)}, (pstmt) -> {
            ResultSet rs = pstmt.executeQuery();
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            } else {
            }
            return rtnUser;
        });
    }
}