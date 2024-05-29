package org.minispring.core.web;


import lombok.Setter;

public class InternalResourceViewResolver implements ViewResolver {
    @Setter
    private Class<?> viewClass = null;
    private String viewClassName = "";
    private String prefix = "";
    private String suffix = "";
    @Setter
    private String contentType;

    public InternalResourceViewResolver() {
        if (getViewClass() == null) {
            setViewClass(JstlView.class);
        }
    }

    public InternalResourceViewResolver(String viewClassName, String prefix, String suffix) {
        setViewClass(JstlView.class);
        setPrefix(prefix);
        setSuffix(suffix);
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
        Class<?> clz = null;
        try {
            clz = Class.forName(viewClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setViewClass(clz);
    }

    protected String getViewClassName() {
        return this.viewClassName;
    }

    protected Class<?> getViewClass() {
        return this.viewClass;
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    protected String getSuffix() {
        return this.suffix;
    }

    protected String getContentType() {
        return this.contentType;
    }

    @Override
    public View resolveViewName(String viewName) throws Exception {
        return buildView(viewName);
    }

    protected View buildView(String viewName) throws Exception {
        Class<?> viewClass = getViewClass();

        View view = (View) viewClass.newInstance();
        view.setUrl(getPrefix() + viewName + getSuffix());

        String contentType = getContentType();
        view.setContentType(contentType);

        return view;
    }
}