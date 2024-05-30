package org.minispring.jdbc;

import org.minispring.app.entity.User;
import org.minispring.core.jdbc.JdbcTemplate;
import org.minispring.core.jdbc.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserJdbcImpl extends JdbcTemplate {

    public UserJdbcImpl(SingleConnectionDataSource ds) {
        super(ds);
    }

    @Override
    protected Object doInStatement(ResultSet rs) {
        //从jdbc数据集读取数据，并生成对象返回
        User rtnUser = null;
        try {
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(rs.getInt("id"));
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rtnUser;
    }
}