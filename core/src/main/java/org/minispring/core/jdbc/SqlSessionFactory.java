package org.minispring.core.jdbc;

public interface SqlSessionFactory {

    SqlSession openSession();

    MapperNode getMapperNode(String name);
}