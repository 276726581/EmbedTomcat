package com.timogroup.tomcat.config;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Arrays;

/**
 * Created by TimoRD on 2016/7/8.
 */
public class FilterConfig {

    private String filterName;
    private Class<? extends Filter> filterClass;
    private InitParameter initParameter;
    private boolean asyncSupported;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Class<? extends Filter> getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    public InitParameter getInitParameter() {
        return initParameter;
    }

    public void setInitParameter(InitParameter initParameter) {
        this.initParameter = initParameter;
    }

    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public FilterConfig() {
    }

    @Override
    public String toString() {
        return "FilterConfig{" +
                "filterName='" + filterName + '\'' +
                ", filterClass=" + filterClass +
                ", initParameter=" + initParameter +
                ", asyncSupported=" + asyncSupported +
                '}';
    }
}
