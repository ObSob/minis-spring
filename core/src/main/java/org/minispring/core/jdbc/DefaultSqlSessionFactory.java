package org.minispring.core.jdbc;

import lombok.Getter;
import lombok.Setter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.minispring.core.bean.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultSqlSessionFactory implements SqlSessionFactory {
    @Autowired
    @Setter
    JdbcTemplate jdbcTemplate;

    @Setter
    @Getter
    String mapperLocations;

    @Getter
    Map<String, MapperNode> mapperNodeMap = new HashMap<>();

    //	private DataSource dataSource;
//	
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
//
//	public DataSource getDataSource() {
//		return this.dataSource;
//	}

    public DefaultSqlSessionFactory(String mapperLocations) {
        this.mapperLocations = mapperLocations;
        init();
    }

    public void init() {
        scanLocation(this.mapperLocations);
        for (Map.Entry<String, MapperNode> entry : this.mapperNodeMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    @Override
    public SqlSession openSession() {
        SqlSession newSqlSession = new DefaultSqlSession();
        newSqlSession.setJdbcTemplate(jdbcTemplate);
        newSqlSession.setSqlSessionFactory(this);

        return newSqlSession;
    }


    private void scanLocation(String location) {
        String sLocationPath = this.getClass().getClassLoader().getResource("").getPath() + location;
        //URL url  =this.getClass().getClassLoader().getResource("/"+location);
        System.out.println("mapper location : " + sLocationPath);
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanLocation(location + "/" + file.getName());
            } else {
                buildMapperNodes(location + "/" + file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath) {
        System.out.println(filePath);
        SAXBuilder saxBuilder = new SAXBuilder();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        try {
            Document document = saxBuilder.build(xmlPath);
            Element rootElement = document.getRootElement();

            String namespace = rootElement.getAttributeValue("namespace");

            List<Element> nodes = rootElement.getChildren();
            for (Element node : nodes) {
                String id = node.getAttributeValue("id");
                String parameterType = node.getAttributeValue("parameterType");
                String resultType = node.getAttributeValue("resultType");
                String sql = node.getTextTrim();

                MapperNode selectNode = new MapperNode();
                selectNode.setNamespace(namespace);
                selectNode.setId(id);
                selectNode.setParameterType(parameterType);
                selectNode.setResultType(resultType);
                selectNode.setSql(sql);
                selectNode.setParameter("");

                this.mapperNodeMap.put(namespace + "." + id, selectNode);
            }
        } catch (JDOMException | IOException ex) {
            ex.printStackTrace();
        }
        return this.mapperNodeMap;
    }

    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

}
