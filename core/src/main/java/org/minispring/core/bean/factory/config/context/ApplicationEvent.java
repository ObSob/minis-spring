package org.minispring.core.bean.factory.config.context;

import java.io.Serial;
import java.io.Serializable;

public class ApplicationEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String msg = null;

    public ApplicationEvent(Object arg0) {
        super();
        this.msg = arg0.toString();
    }
}
