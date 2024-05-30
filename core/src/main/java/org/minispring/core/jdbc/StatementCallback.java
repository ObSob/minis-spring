package org.minispring.core.jdbc;



import java.sql.Statement;

import java.sql.SQLException;

public interface StatementCallback {
    Object doInStatement(Statement stmt) throws SQLException;
}
