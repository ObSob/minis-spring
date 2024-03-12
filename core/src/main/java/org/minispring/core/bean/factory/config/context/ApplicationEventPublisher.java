package org.minispring.core.bean.factory.config.context;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
    void addApplicationListener(ApplicationListener listener);
}
