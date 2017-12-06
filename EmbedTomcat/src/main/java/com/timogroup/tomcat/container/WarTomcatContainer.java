package com.timogroup.tomcat.container;

import org.apache.catalina.Container;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;

import java.io.File;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class WarTomcatContainer extends AbstractTomcatContainer {

    private File war;
    private File baseDir;

    public WarTomcatContainer(File war) {
        this.war = war;
    }

    @Override
    protected Tomcat createTomcat() {
        String dir = String.format("%s.%d", getDisplayName(), getPort());
        baseDir = new File(war.getParent(), dir);

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.setPort(getPort());
        Connector connector = new Connector(Http11NioProtocol);
        customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getService().addConnector(connector);

        return tomcat;
    }

    private void customizeConnector(Connector connector) {
        connector.setPort(getPort());
        connector.setURIEncoding(getEncoding());
        connector.setEnableLookups(false);
        connector.setAsyncTimeout(30 * 1000);
        connector.setMaxHeaderCount(10 * 1024);
        connector.setMaxParameterCount(10 * 1024);
        connector.setMaxPostSize(10 * 1024 * 1024);
        connector.setMaxSavePostSize(10 * 1024);

        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        protocol.setTcpNoDelay(true);

        protocol.setSelectorTimeout(1000);
        protocol.setConnectionTimeout(getConnectionTimeout());
        protocol.setKeepAliveTimeout(getConnectionTimeout());
        protocol.setConnectionUploadTimeout(2 * getConnectionTimeout());

        protocol.setMaxHeaderCount(10 * 1024);
        protocol.setMaxHttpHeaderSize(10 * 1024);
        protocol.setMaxSwallowSize(5 * 1024 * 1024);
        protocol.setMaxSavePostSize(10 * 1024);

        protocol.setBacklog(1024);
        protocol.setMaxKeepAliveRequests(100);
        protocol.setPollerThreadCount(Runtime.getRuntime().availableProcessors());
        protocol.setAcceptorThreadCount(Runtime.getRuntime().availableProcessors());
        protocol.setMinSpareThreads(getMinThreads());
        protocol.setMaxThreads(getMaxThreads());
        protocol.setProcessorCache(getMinThreads());
        protocol.setMaxConnections(getMaxConnections());
    }

    @Override
    protected void onStart(Tomcat tomcat) {
        if (!isWar(war.getAbsolutePath()) && !isWarPath(war.getAbsoluteFile())) {
            throw new RuntimeException("not found war");
        }

        try {
            StandardHost host = (StandardHost) tomcat.getHost();
            host.setAppBase(baseDir.getAbsolutePath());
            host.setUnpackWARs(true);
            host.setAutoDeploy(true);

            StandardContext context = (StandardContext) tomcat.addWebapp(getPath(), war.getAbsolutePath());
            context.setUnpackWAR(true);
            context.setReloadable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStarted(Tomcat tomcat) {
        Container[] containers = tomcat.getHost().findChildren();
        for (Container context : containers) {
            StandardContext standardContext = (StandardContext) context;
            WebResourceRoot resources = standardContext.getResources();
            resources.setCachingAllowed(true);
            resources.setCacheMaxSize(100 * 1024 * 1024);
        }
    }

    private boolean isWar(String war) {
        if (war == null || war.length() == 0) {
            return false;
        }
        int index = war.lastIndexOf(".war");
        if (-1 == index) {
            return false;
        }

        return true;
    }

    private boolean isWarPath(File war) {
        File webXml = new File(war, "/WEB-INF/web.xml");
        if (webXml.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void showBanner() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("*       WarTomcatContainer       *" + System.lineSeparator());
        buffer.append("*                                *" + System.lineSeparator());
        buffer.append("**********************************" + System.lineSeparator());
        buffer.append(String.format("DisplayName: %s", getDisplayName()) + System.lineSeparator());
        buffer.append(String.format("Port: %d", getPort()) + System.lineSeparator());
        buffer.append(String.format("War: %s", war.getAbsolutePath()) + System.lineSeparator());
        buffer.append(String.format("Path: %s", getPath()) + System.lineSeparator());
        buffer.append(String.format("MixThreads: %d", getMinThreads()) + System.lineSeparator());
        buffer.append(String.format("MaxThreads: %d", getMaxThreads()) + System.lineSeparator());
        buffer.append(String.format("MaxConnections: %d", getMaxConnections()) + System.lineSeparator());
        buffer.append(String.format("Timeout: %s", getConnectionTimeout()) + System.lineSeparator());
        System.out.println(buffer.toString());
    }
}
