package com.timogroup.tomcat.config;

import javax.servlet.ServletContextListener;

/**
 * Created by TimoRD on 2016/7/8.
 */
public class ListenerConfig {

    private Class<? extends ServletContextListener> listenerClass;
    private InitParameter initParameter;

    public Class<? extends ServletContextListener> getListenerClass() {
        return listenerClass;
    }

    public void setListenerClass(Class<? extends ServletContextListener> listenerClass) {
        this.listenerClass = listenerClass;
    }

    public InitParameter getInitParameter() {
        return initParameter;
    }

    public void setInitParameter(InitParameter initParameter) {
        this.initParameter = initParameter;
    }

    public ListenerConfig() {
    }

    @Override
    public String toString() {
        return "ListenerConfig{" +
                "listenerClass=" + listenerClass +
                ", initParameter=" + initParameter +
                '}';
    }
}
