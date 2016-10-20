package com.timogroup.tomcat;

import com.timogroup.tomcat.config.ContextInitializer;
import com.timogroup.tomcat.config.ServletContextInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class EmbedTomcat {

    private static final String Protocol = "org.apache.coyote.http11.Http11NioProtocol";

    private List<ContextInitializer> contextInitializerList = new ArrayList<>();
    private List<ServletContextInitializer> servletContextInitializerList = new ArrayList<>();

    private Tomcat tomcat;
    private String displayName = "tomcat";
    private int port = 8080;
    private int maxThreads = 200;
    private int maxConnections = 10000;
    private int connectionTimeout = 30 * 1000;
    private String encoding = "utf-8";

    public Tomcat getTomcat() {
        return tomcat;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void addContextInitializer(ContextInitializer initializer) {
        contextInitializerList.add(initializer);
    }

    public void addServletContextInitializer(ServletContextInitializer initializer) {
        servletContextInitializerList.add(initializer);
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
                for (ServletContextInitializer servletContextInitializer : servletContextInitializerList) {
                    servletContextInitializer.onStartup(ctx);
                }
            }
        };
        context.addServletContainerInitializer(initializer, Collections.emptySet());

        for (ContextInitializer contextInitializer : contextInitializerList) {
            contextInitializer.onStartup(context);
        }
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
