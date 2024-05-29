package org.minispring.core.bean.factory.config.context;

import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.minispring.core.bean.annotation.AutowiredAnnotationBeanPostProcessor;
import org.minispring.core.bean.exception.BeansException;
import org.minispring.core.bean.factory.config.BeanDefinition;
import org.minispring.core.bean.factory.config.ConfigurableListableBeanFactory;
import org.minispring.core.bean.factory.config.ConstructorArgumentValues;
import org.minispring.core.bean.factory.config.DefaultListableBeanFactory;
import org.minispring.core.bean.factory.support.AbstractBeanFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
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
                log.info("Bean ID : " + id);
                log.info("Bean Class : " + className);
                BeanDefinition beanDefinition = new BeanDefinition(id, className);
                List<Element> propertyList = bean.getChildren("property");
                ConstructorArgumentValues avs = new ConstructorArgumentValues();
                for (Element property : propertyList) {
                    String type = property.getAttributeValue("type");
                    String name = property.getAttributeValue("name");
                    String value = property.getAttributeValue("value");
                    log.info("Property Type : " + type);
                    log.info("Property Name : " + name);
                    log.info("Property Value : " + value);
                    avs.addArgumentValue(type, name, value);
                }
                beanDefinition.setConstructorArgumentValues(avs);
                this.beanFactory.registerBeanDefinition(id, beanDefinition);
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
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return (ConfigurableListableBeanFactory) this.beanFactory;
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
