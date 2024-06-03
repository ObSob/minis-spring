package org.minispring.aop;

public class RealAction implements Action {
    @Override
    public void execute() {
        System.out.println("real action");
    }
}
