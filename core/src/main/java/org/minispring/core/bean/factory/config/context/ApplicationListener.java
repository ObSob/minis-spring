package org.minispring.core.bean.factory.config.context;

import lombok.extern.slf4j.Slf4j;

import java.util.EventListener;

@Slf4j
public class ApplicationListener implements EventListener {

    void onApplicationEvent(ApplicationEvent event) {
        log.info(event.toString());
    }
}
