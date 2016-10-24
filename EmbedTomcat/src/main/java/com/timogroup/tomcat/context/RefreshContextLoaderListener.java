package com.timogroup.tomcat.context;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by TimoRD on 2016/10/24.
 */
public class RefreshContextLoaderListener extends ContextLoaderListener {

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        WebApplicationContext applicationContext = super.initWebApplicationContext(servletContext);
        ApplicationContextUtil.addApplicationContext(ApplicationContextUtil.ContextLoaderListener, (AbstractRefreshableConfigApplicationContext) applicationContext);
        return applicationContext;
    }
}
