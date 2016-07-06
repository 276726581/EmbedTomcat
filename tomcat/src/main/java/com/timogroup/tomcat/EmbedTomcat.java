package com.timogroup.tomcat;

import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Collections;
import java.util.Set;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private String appXml;
    private String servletXml;
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EmbedTomcat(String appXml) {
        this.appXml = appXml;
        this.servletXml = "";
    }

    public EmbedTomcat(String appXml, String servletXml) {
        this.appXml = appXml;
        this.servletXml = servletXml;
    }

    public Container createContainer() {
        ServletContainerInitializer initializer = new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
                ctx.setInitParameter("contextConfigLocation", appXml);
                ctx.addListener(ContextLoaderListener.class);

                ServletRegistration.Dynamic servlet = ctx.addServlet("dispatcherServlet", DispatcherServlet.class);
                servlet.setInitParameter("contextConfigLocation", servletXml);
                servlet.addMapping("/");
                servlet.setLoadOnStartup(0);
            }
        };
        StandardContext context = new StandardContext();
        context.setPath("/");
        context.addServletContainerInitializer(initializer, Collections.emptySet());
        context.addLifecycleListener(new Tomcat.FixContextListener());

        return context;
    }

    public synchronized void startAwait() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
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
