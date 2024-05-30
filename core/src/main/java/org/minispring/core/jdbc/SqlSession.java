package org.minispring.core.jdbc;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

    Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}
