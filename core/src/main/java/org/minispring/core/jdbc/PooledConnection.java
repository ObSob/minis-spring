//package org.minispring.core.jdbc;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class PooledConnection implements Connection {
//    private Connection connection;
//    private boolean active;
//
//    public PooledConnection() {
//    }
//
//    public PooledConnection(Connection connection, boolean active) {
//        this.connection = connection;
//        this.active = active;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//
//    public void setConnection(Connection connection) {
//        this.connection = connection;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//    public void close() throws SQLException {
//        this.active = false;
//    }
//
//    @Override
//    public PreparedStatement prepareStatement(String sql) throws SQLException {
//        return this.connection.prepareStatement(sql);
//    }
//}