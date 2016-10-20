package com.timogroup.tomcat.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by TimoRD on 2016/10/20.
 */
public interface ServletContextInitializer {

    void onStartup(ServletContext context) throws ServletException;
}
