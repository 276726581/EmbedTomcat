package com.test;


import com.timogroup.tomcat.config.DefaultContextInitializer;
import com.timogroup.tomcat.config.SpringInitializer;
import com.timogroup.tomcat.container.EmbedTomcatContainer;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        EmbedTomcatContainer tomcat = new EmbedTomcatContainer();
        tomcat.setDisplayName("tomcat");
        tomcat.setPort(8000);
        tomcat.setMaxThreads(500);
        tomcat.addContextInitializer(new DefaultContextInitializer());
        SpringInitializer springInitializer = new SpringInitializer();
        springInitializer.setContextConfig("classpath:app.xml");
        springInitializer.setDispatcherConfig("classpath:mvc.xml");
        tomcat.addServletContextInitializer(springInitializer);
        tomcat.startAwait();
    }
}
