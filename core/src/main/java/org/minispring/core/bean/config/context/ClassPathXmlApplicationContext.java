package org.minispring.core.bean.config.context;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.minispring.core.bean.config.BeanDefinition;
import org.minispring.core.bean.config.DefaultBeanDefinition;
import org.minispring.core.bean.config.factory.BeanFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements ApplicationContext, BeanFactory {

    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private final Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String configLocation) {
        // Load bean definitions from XML file
        loadBeanDefinitions(configLocation);
        // Instantiate and initialize beans
        instantiateBeans();
    }

    private void instantiateBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = null;
            try {
                bean = createBean(beanDefinition);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            singletons.put(beanDefinition.getBeanClassName(), bean);
        }
    }

    private Object createBean(BeanDefinition beanDefinition) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("org.minispring.service.HelloServiceImpl");
        return clazz.newInstance();
    }

    private void loadBeanDefinitions(String configLocation) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            InputStream in = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configLocation);
            Document document = saxBuilder.build(in);
            Element rootElement = document.getRootElement();

            List<Element> beanList = rootElement.getChildren("bean");
            for (Element bean : beanList) {
                String id = bean.getAttributeValue("id");
                String className = bean.getAttributeValue("class");
                System.out.println("Bean ID : " + id);
                System.out.println("Bean Class : " + className);

                List<Element> propertyList = bean.getChildren("property");
                for (Element property : propertyList) {
                    String type = property.getAttributeValue("type");
                    String name = property.getAttributeValue("name");
                    System.out.println("Property Type : " + type);
                    System.out.println("Property Name : " + name);
                }
                beanDefinitions.add(new DefaultBeanDefinition(className));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return singletons.get(name);
    }

    @Override
    public boolean containsBean(String name) {
        return singletons.containsKey(name);
    }

    @Override
    public String getApplicationName() {
        return "";
    }
}
