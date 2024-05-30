package org.minispring.core.jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;

@Setter
@Getter
public class DefaultSqlSession implements SqlSession {
//	private DataSource dataSource;
//	
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
//
//	public DataSource getDataSource() {
//		return this.dataSource;
//	}

    JdbcTemplate jdbcTemplate;

    SqlSessionFactory sqlSessionFactory;



    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        System.out.println(sqlid);
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        System.out.println(sql);

        return jdbcTemplate.query(sql, args, pstmtcallback);
    }

    private void buildParameter() {
    }

    private Object resultSet2Obj() {
        return null;
    }

}
