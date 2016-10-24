package com.timogroup.tomcat.config;

import com.timogroup.tomcat.context.RefreshContextLoaderListener;
import com.timogroup.tomcat.context.RefreshDispatcherServlet;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by TimoRD on 2016/10/20.
 */
public class SpringInitializer implements ServletContextInitializer {

    private String contextConfig = "classpath:app.xml";
    private String dispatcherConfig = "classpath:dispatcher.xml";
    private String encoding = "utf-8";
    private boolean asyncSupported = true;

    public String getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(String contextConfig) {
        this.contextConfig = contextConfig;
    }

    public String getDispatcherConfig() {
        return dispatcherConfig;
    }

    public void setDispatcherConfig(String dispatcherConfig) {
        this.dispatcherConfig = dispatcherConfig;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public SpringInitializer() {
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        initContextLoaderListener(context);
        initCharacterEncodingFilter(context);
        initDispatcherServlet(context);
    }

    private void initContextLoaderListener(ServletContext context) {
        context.setInitParameter("contextConfigLocation", contextConfig);
        context.addListener(RefreshContextLoaderListener.class);
    }

    private void initCharacterEncodingFilter(ServletContext context) {
        FilterRegistration.Dynamic filter = context.addFilter("encoding", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", encoding);
        filter.setAsyncSupported(asyncSupported);
    }

    private void initDispatcherServlet(ServletContext context) {
        ServletRegistration.Dynamic servlet = context.addServlet("dispatcherServlet", RefreshDispatcherServlet.class);
        servlet.setInitParameter("contextConfigLocation", dispatcherConfig);
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(asyncSupported);
    }
}
