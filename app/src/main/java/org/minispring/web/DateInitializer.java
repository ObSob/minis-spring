package org.minispring.web;

import org.minispring.core.web.WebBindingInitializer;
import org.minispring.core.web.WebDataBinder;

import java.util.Date;

public class DateInitializer implements WebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class, "yyyy-MM-dd", false));
    }
}