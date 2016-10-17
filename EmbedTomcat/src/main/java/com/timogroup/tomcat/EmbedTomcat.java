package com.timogroup.tomcat;

import com.timogroup.tomcat.config.FilterConfig;
import com.timogroup.tomcat.config.InitParameter;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;
import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.*;
import java.util.*;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private List<InitParameter> parameterList = new ArrayList<>();
    private List<ListenerConfig> listenerList = new ArrayList<>();
    private List<FilterConfig> filterList = new ArrayList<>();
    private List<ServletConfig> servletList = new ArrayList<>();

    private Tomcat tomcat;
    private String displayName;
    private int port;

    public Tomcat getTomcat() {
        return tomcat;
    }

    public void addContextParameter(InitParameter parameter) {
        parameterList.add(parameter);
    }

    public void addListener(ListenerConfig listener) {
        listenerList.add(listener);
    }

    public void addFilter(FilterConfig filter) {
        filterList.add(filter);
    }

    public void addServlet(ServletConfig servletConfig) {
        servletList.add(servletConfig);
    }

    public void enableSpringMVC(String contextConfig, String servletConfig, String encoding) {
        ListenerConfig contextLoaderListener = DefaultFactory.getDefaultContextLoaderListener(contextConfig);
        addListener(contextLoaderListener);

        FilterConfig filter = DefaultFactory.getDefaultCharacterEncodingFilter(encoding);
        addFilter(filter);

        ServletConfig dispatcherServlet = DefaultFactory.getDefaultDispatcherServlet(servletConfig);
        dispatcherServlet.setAsyncSupported(true);
        addServlet(dispatcherServlet);
    }

    public EmbedTomcat(String displayName, int port) {
        this.tomcat = new Tomcat();
        this.displayName = displayName;
        this.port = port;
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
                    ctx.addListener(listenerConfig.getListenerClass());
                }

                for (FilterConfig filterConfig : filterList) {
                    FilterRegistration.Dynamic filter = ctx.addFilter(filterConfig.getFilterName(), filterConfig.getFilterClass());
                    InitParameter initParameter = filterConfig.getInitParameter();
                    if (null != initParameter) {
                        filter.setInitParameter(initParameter.getName(), initParameter.getValue());
                    }
                    filter.setAsyncSupported(filterConfig.isAsyncSupported());
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
        context.addLifecycleListener(new Tomcat.FixContextListener());

        Map<String, String> map = DefaultFactory.getDefaultMimeMapping();
        for (String key : map.keySet()) {
            String value = map.get(key);
            context.addMimeMapping(key, value);
        }

        return context;
    }

    public synchronized void startAwait() throws LifecycleException {
        tomcat.getHost().addChild(createContainer());
        tomcat.getHost().setAutoDeploy(false);
        tomcat.setPort(port);

        tomcat.start();
        showLog();
        tomcat.getServer().await();
    }

    private void showLog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("*    EmbedTomcat Application     *" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append(String.format("DisplayName: %s", displayName) + System.lineSeparator());
        buffer.append(String.format("Port: %d", port) + System.lineSeparator());
        System.out.println(buffer.toString());
    }
}
