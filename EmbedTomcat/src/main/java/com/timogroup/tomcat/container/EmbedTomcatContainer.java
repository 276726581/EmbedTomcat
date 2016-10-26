package com.timogroup.tomcat.container;

import com.timogroup.tomcat.config.ContextInitializer;
import com.timogroup.tomcat.config.ServletContextInitializer;
import org.apache.catalina.Context;
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
 * Created by TimoRD on 2016/10/26.
 */
public class EmbedTomcatContainer extends AbstractTomcatContainer {

    private String docBase;
    private List<ContextInitializer> contextInitializerList = new ArrayList<>();
    private List<ServletContextInitializer> servletContextInitializerList = new ArrayList<>();

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public void addContextInitializer(ContextInitializer initializer) {
        contextInitializerList.add(initializer);
    }

    public void addServletContextInitializer(ServletContextInitializer initializer) {
        servletContextInitializerList.add(initializer);
    }

    @Override
    protected Tomcat createTomcat() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(getPort());
        Connector connector = new Connector(Http11NioProtocol);
        org.apache.coyote.http11.Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        protocol.setMaxThreads(getMaxThreads());
        protocol.setMaxConnections(getMaxConnections());
        protocol.setConnectionTimeout(getConnectionTimeout());
        connector.setPort(getPort());
        connector.setURIEncoding(getEncoding());
        tomcat.setConnector(connector);
        tomcat.getService().addConnector(connector);

        return tomcat;
    }

    @Override
    protected void onStart(Tomcat tomcat) {
        Context context = tomcat.addContext("/", docBase);
        initTomcatContext(context);
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

    @Override
    protected void showBanner() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("*      EmbedTomcatContainer      *" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append(String.format("DisplayName: %s", getDisplayName()) + System.lineSeparator());
        buffer.append(String.format("Port: %d", getPort()) + System.lineSeparator());
        System.out.println(buffer.toString());
    }
}
