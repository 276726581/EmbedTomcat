package com.timogroup.tomcat.config;

import javax.servlet.Servlet;
import java.util.Arrays;

/**
 * Created by TimoRD on 2016/7/8.
 */
public class ServletConfig {

    private String servletName;
    private Class<? extends Servlet> servletClass;
    private InitParameter initParameter;
    private String[] urlPatterns;
    private int loadOnStartup;
    private boolean asyncSupported;

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public Class<? extends Servlet> getServletClass() {
        return servletClass;
    }

    public void setServletClass(Class<? extends Servlet> servletClass) {
        this.servletClass = servletClass;
    }

    public InitParameter getInitParameter() {
        return initParameter;
    }

    public void setInitParameter(InitParameter initParameter) {
        this.initParameter = initParameter;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String... urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public ServletConfig() {
    }

    @Override
    public String toString() {
        return "ServletConfig{" +
                "servletName='" + servletName + '\'' +
                ", servletClass=" + servletClass +
                ", initParameter=" + initParameter +
                ", urlPatterns=" + Arrays.toString(urlPatterns) +
                ", loadOnStartup=" + loadOnStartup +
                ", asyncSupported=" + asyncSupported +
                '}';
    }
}
