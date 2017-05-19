package com.timogroup.tomcat.container;

import org.apache.catalina.startup.Tomcat;

/**
 * Created by TimoRD on 2016/10/26.
 */
public abstract class AbstractTomcatContainer {

    protected static final String Http11NioProtocol = "org.apache.coyote.http11.Http11NioProtocol";

    private Tomcat tomcat;
    private String displayName = "tomcat";
    private int port = 8080;
    private String path = "/";
    private int minThreads = 200;
    private int maxThreads = 300;
    private int maxConnections = 10000;
    private int connectionTimeout = 30 * 1000;
    private String encoding = "utf-8";

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(int minThreads) {
        this.minThreads = minThreads;
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

    public AbstractTomcatContainer() {

    }

    public Tomcat getTomcat() {
        if (null == tomcat) {
            tomcat = createTomcat();
        }

        return tomcat;
    }

    public synchronized void startAwait() throws Exception {
        Tomcat tomcat = getTomcat();
        onStart(tomcat);
        tomcat.start();
        showBanner();
        onStarted(tomcat);
        tomcat.getServer().await();
        onDestroy(tomcat);
    }

    protected void onStart(Tomcat tomcat) {

    }

    protected void onStarted(Tomcat tomcat) {

    }

    protected void onDestroy(Tomcat tomcat) {

    }

    protected abstract Tomcat createTomcat();

    protected void showBanner() {

    }
}
