package org.minispring.core.bean.factory.config.context;

import java.io.Serial;

public class ContextRefreshEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    public String toString() {
        return this.msg;
    }

}
