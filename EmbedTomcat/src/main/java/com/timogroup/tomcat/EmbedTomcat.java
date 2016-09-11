package com.timogroup.tomcat;

import com.timogroup.tomcat.config.InitParameter;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;
import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.*;
import org.apache.catalina.mbeans.GlobalResourcesLifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.VersionLoggerListener;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.*;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private List<InitParameter> parameterList = new ArrayList<>();
    private List<ListenerConfig> listenerList = new ArrayList<>();
    private List<ServletConfig> servletList = new ArrayList<>();
    private int port;

    public void addContextParameter(InitParameter parameter) {
        parameterList.add(parameter);
    }

    public void addListener(ListenerConfig listener) {
        listenerList.add(listener);
    }

    public void addServlet(ServletConfig servletConfig) {
        servletList.add(servletConfig);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EmbedTomcat() {
    }

    public Container createContainer() {
        ServletContainerInitializer initializer = new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
                for (InitParameter initParameter : parameterList) {
                    ctx.setInitParameter(initParameter.getName(), initParameter.getValue());
                }

                for (ListenerConfig listenerConfig : listenerList) {
                    InitParameter initParameter = listenerConfig.getInitParameter();
                    if (null != initParameter) {
                        ctx.setInitParameter(initParameter.getName(), initParameter.getValue());
                    }
                }

                for (ListenerConfig listenerConfig : listenerList) {
                    ctx.addListener(listenerConfig.getListenerClass());
                }

                for (ServletConfig servletConfig : servletList) {
                    ServletRegistration.Dynamic servlet = ctx.addServlet(servletConfig.getServletName(), servletConfig.getServletClass());
                    InitParameter initParameter = servletConfig.getInitParameter();
                    if (null != initParameter) {
                        servlet.setInitParameter(initParameter.getName(), initParameter.getValue());
                    }
                    servlet.addMapping(servletConfig.getUrlPatterns());
                    servlet.setLoadOnStartup(servletConfig.getLoadOnStartup());
                    servlet.setAsyncSupported(servletConfig.isAsyncSupported());
                }
            }
        };
        StandardContext context = new StandardContext();
        context.setPath("/");
        context.addServletContainerInitializer(initializer, Collections.emptySet());
        context.addLifecycleListener(new VersionLoggerListener());
        context.addLifecycleListener(new AprLifecycleListener());
        context.addLifecycleListener(new JreMemoryLeakPreventionListener());
        context.addLifecycleListener(new GlobalResourcesLifecycleListener());
        context.addLifecycleListener(new ThreadLocalLeakPreventionListener());
        context.addLifecycleListener(new Tomcat.FixContextListener());

        Map<String, String> map = DefaultFactory.getDefaultMimeMapping();
        for (String key : map.keySet()) {
            String value = map.get(key);
            context.addMimeMapping(key, value);
        }

        return context;
    }

    public synchronized void startAwait() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getService().addExecutor(new StandardThreadExecutor());
        tomcat.getHost().addChild(createContainer());
        tomcat.getHost().setAutoDeploy(false);

        tomcat.start();
        showLog();
        tomcat.getServer().await();
    }

    private void showLog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("**********************************\n");
        buffer.append("*                                *\n");
        buffer.append("*    EmbedTomcat Application     *\n");
        buffer.append("*                                *\n");
        buffer.append("**********************************\n");
        System.out.println(buffer.toString());
    }
}
