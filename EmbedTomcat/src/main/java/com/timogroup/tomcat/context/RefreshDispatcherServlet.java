package com.timogroup.tomcat.context;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by TimoRD on 2016/10/24.
 */
public class RefreshDispatcherServlet extends DispatcherServlet {

    @Override
    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext applicationContext = super.initWebApplicationContext();
        ApplicationContextUtil.addApplicationContext(ApplicationContextUtil.DispatcherServlet, (AbstractRefreshableConfigApplicationContext) applicationContext);
        return applicationContext;
    }
}
