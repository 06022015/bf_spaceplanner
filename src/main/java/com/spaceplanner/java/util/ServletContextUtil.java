package com.spaceplanner.java.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 28/7/14
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ServletContextUtil implements ServletContextAware {
    private static ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        ServletContextUtil.servletContext=servletContext;
    }

    public static ServletContext getServletContext(){
        return servletContext;
    }
}
