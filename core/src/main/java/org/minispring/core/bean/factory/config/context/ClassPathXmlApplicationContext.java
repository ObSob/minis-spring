package org.minispring.core.bean.factory.config.context;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.minispring.core.bean.annotation.AutowiredAnnotationBeanPostProcessor;
import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.config.BeanDefinition;
import org.minispring.core.bean.factory.config.ConfigurableListableFactory;
import org.minispring.core.bean.factory.config.DefaultListableBeanFactory;
import org.minispring.core.bean.factory.support.AbstractBeanFactory;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    AbstractBeanFactory beanFactory = new DefaultListableBeanFactory();

    private final List<String> configLocations = new ArrayList<>();

    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();

    private final Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String configLocation) {
        this(configLocation, true);
    }

    public ClassPathXmlApplicationContext(String configLocation, boolean refresh)
            throws BeansException {
        setConfigLocations(new String[]{configLocation});
        loadBeanDefinitions(configLocation);

//        super(parent);
        if (refresh) {
            refresh();
        }
    }


    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableFactory bf) {

    }

    @Override
    void registerBeanPostProcessors(ConfigurableListableFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    private void instantiateBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = null;
            try {
                bean = createBean(beanDefinition);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            singletons.put(beanDefinition.getClassName(), bean);
        }
    }

    private Object createBean(BeanDefinition beanDefinition) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(beanDefinition.getClassName());
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
                this.beanFactory.registerBeanDefinition(id, new BeanDefinition(id, className));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    @Override
    public boolean containsBean(String name) {
        return singletons.containsKey(name);
    }

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public ConfigurableListableFactory getBeanFactory() throws IllegalStateException {
        return (ConfigurableListableFactory) this.beanFactory;
    }

    private void setConfigLocations(String[] configLocations) {
        this.configLocations.addAll(List.of(configLocations));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
