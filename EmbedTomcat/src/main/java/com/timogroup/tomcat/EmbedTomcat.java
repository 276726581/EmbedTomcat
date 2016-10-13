package com.timogroup.tomcat;

import com.timogroup.tomcat.config.InitParameter;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;
import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private List<InitParameter> parameterList = new ArrayList<>();
    private List<ListenerConfig> listenerList = new ArrayList<>();
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

    public void addServlet(ServletConfig servletConfig) {
        servletList.add(servletConfig);
    }

    public void enableSpringMVC(String contextConfig, String servletConfig) {
        ListenerConfig contextLoaderListener = DefaultFactory.getDefaultContextLoaderListener(contextConfig);
        addListener(contextLoaderListener);
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
        tomcat.getService().addConnector(getConnector());
        tomcat.getHost().setAutoDeploy(false);

        tomcat.start();
        showLog();
        tomcat.getServer().await();
    }

    private Connector getConnector() {
        Connector connector = new Connector(DEFAULT_PROTOCOL);
        connector.setURIEncoding(DEFAULT_CHARSET.name());
        connector.setPort(port);
        return connector;
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
