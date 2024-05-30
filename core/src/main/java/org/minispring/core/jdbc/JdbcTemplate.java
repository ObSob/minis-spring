package org.minispring.core.jdbc;


import org.minispring.core.bean.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public abstract class JdbcTemplate {

    private String url = "jdbc:mysql://localhost:3306/minispring?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private final String user = "root";
    private final String password = "123456";

    @Autowired
    private SingleConnectionDataSource dataSource;

    public JdbcTemplate(SingleConnectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object query(String sql, Object[] args, PreparedStatementCallback callback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement(sql);
            //通过argumentSetter统一设置参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            return callback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
            }
        }

        return null;
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            con = dataSource.getConnection();

            //准备SQL命令语句
            pstmt = con.prepareStatement(sql);
            //设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            //执行语句
            rs = pstmt.executeQuery();

            //数据库结果集映射为对象列表，返回
            return resultExtractor.extractData(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public Connection getConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", user);
        connectionProps.put("password", password);
        return DriverManager.getConnection(url, connectionProps);
    }

    protected abstract Object doInStatement(ResultSet rs);
}
