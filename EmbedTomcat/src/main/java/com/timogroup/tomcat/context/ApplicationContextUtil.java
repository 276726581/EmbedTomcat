package com.timogroup.tomcat.context;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by TimoRD on 2016/10/24.
 */
public final class ApplicationContextUtil {

    public static final String ContextLoaderListener = "ContextLoaderListener";
    public static final String DispatcherServlet = "DispatcherServlet";

    private static Map<String, AbstractRefreshableConfigApplicationContext> map = new ConcurrentHashMap<>();

    private ApplicationContextUtil() {

    }

    public static void addApplicationContext(String name, AbstractRefreshableConfigApplicationContext applicationContext) {
        map.put(name, applicationContext);
    }

    public static AbstractRefreshableConfigApplicationContext getApplicationContext(String name) {
        AbstractRefreshableConfigApplicationContext applicationContext = map.get(name);
        return applicationContext;
    }

    public static void refreshApplicationContext() {
        Collection<AbstractRefreshableConfigApplicationContext> collection = map.values();
        for (AbstractRefreshableConfigApplicationContext applicationContext : collection) {
            applicationContext.refresh();
        }
    }
}
