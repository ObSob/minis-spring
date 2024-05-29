package org.minispring.core.web;

public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;

}