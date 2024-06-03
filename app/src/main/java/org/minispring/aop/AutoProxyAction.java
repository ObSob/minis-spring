package org.minispring.aop;

public class AutoProxyAction implements Action {
    @Override
    public void execute() {
        System.out.println("auto proxy action");
    }
}
