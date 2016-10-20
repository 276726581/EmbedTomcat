package com.timogroup.tomcat;

import com.timogroup.tomcat.config.FilterConfig;
import com.timogroup.tomcat.config.InitParameter;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;

import javax.servlet.*;
import java.util.*;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private static final String DefaultServlet = "org.apache.catalina.servlets.DefaultServlet";
    private static final String JspServlet = "org.apache.jasper.servlet.JspServlet";
    private static final String Protocol = "org.apache.coyote.http11.Http11NioProtocol";

    private List<InitParameter> parameterList = new ArrayList<>();
    private List<ListenerConfig> listenerList = new ArrayList<>();
    private List<FilterConfig> filterList = new ArrayList<>();
    private List<ServletConfig> servletList = new ArrayList<>();

    private Tomcat tomcat;
    private String displayName = "tomcat";
    private int port = 8080;
    private int maxThreads = 200;
    private int maxConnections = 10000;
    private int connectionTimeout = 60 * 1000;
    private String encoding = "utf-8";
    private Context defaultServlet;

    public Tomcat getTomcat() {
        return tomcat;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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

    public EmbedTomcat() {
        this.tomcat = new Tomcat();
    }

    public synchronized void startAwait() throws LifecycleException {
        tomcat.setPort(port);
        tomcat.getHost().setAutoDeploy(false);

        Connector connector = new Connector(Protocol);
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        protocol.setMaxThreads(maxThreads);
        protocol.setMaxConnections(maxConnections);
        protocol.setConnectionTimeout(connectionTimeout);
        connector.setPort(port);
        connector.setURIEncoding(encoding);
        tomcat.setConnector(connector);
        tomcat.getService().addConnector(connector);

        Context context = tomcat.addContext("/", null);
        initTomcatContext(context);

        tomcat.start();
        showLog();
        tomcat.getServer().await();
    }

    private void initTomcatContext(Context context) {
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

        context.addServletContainerInitializer(initializer, Collections.emptySet());
        setDefaultServlet(context);
        setJspServlet(context);

        Map<String, String> map = DefaultFactory.getDefaultMimeMapping();
        for (String key : map.keySet()) {
            String value = map.get(key);
            context.addMimeMapping(key, value);
        }
    }

    private void setDefaultServlet(Context context) {
        String name = "default";
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName(name);
        defaultServlet.setServletClass(DefaultServlet);
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMapping("/", name);
    }

    private void setJspServlet(Context context) {
        String name = "jsp";
        Wrapper jspServlet = context.createWrapper();
        jspServlet.setName("jsp");
        jspServlet.setServletClass(JspServlet);
        jspServlet.addInitParameter("fork", "false");
        jspServlet.addInitParameter("xpoweredBy", "false");
        jspServlet.setLoadOnStartup(3);
        context.addChild(jspServlet);
        context.addServletMapping("*.jsp", name);
        context.addServletMapping("*.jspx", name);
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
