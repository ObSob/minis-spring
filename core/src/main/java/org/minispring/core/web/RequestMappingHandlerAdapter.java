package org.minispring.core.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private WebApplicationContext wac;
    private WebBindingInitializer webBindingInitializer = null;
    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
        this.webBindingInitializer = (WebBindingInitializer) this.wac.getBean("webBindingInitializer");
    }

    public RequestMappingHandlerAdapter(HttpMessageConverter messageConverter, WebBindingInitializer webBindingInitializer) {
        this.messageConverter = messageConverter;
        this.webBindingInitializer = webBindingInitializer;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return invokeHandlerMethod(request, response, (HandlerMethod) handler);
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();

        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            Object methodParamObj = methodParameter.getType().newInstance();
            //给这个参数创建
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            webBindingInitializer.initBinder(wdb);
            wdb.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);

        ModelAndView mav = null;
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)) {
            //ResponseBody
            this.messageConverter.write(returnObj, response);
        } else { //返回的是前端页面
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView) returnObj;
            } else if (returnObj instanceof String) {
                //字符串也认为是前端页面
                String sTarget = (String) returnObj;
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }
        return mav;
    }
}