package com.test;


import com.test.servlet.TestServlet;
import com.timogroup.tomcat.config.ContextInitializer;
import com.timogroup.tomcat.config.DefaultContextInitializer;
import com.timogroup.tomcat.config.SpringInitializer;
import com.timogroup.tomcat.container.EmbedTomcatContainer;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;

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

        tomcat.addContextInitializer(new ContextInitializer() {
            @Override
            public void onStartup(Context context) {
                String name = "test";
                Wrapper defaultServlet = context.createWrapper();
                defaultServlet.setName(name);
                defaultServlet.setServletClass(TestServlet.class.getName());
                defaultServlet.setLoadOnStartup(0);
                defaultServlet.setOverridable(true);
                context.addChild(defaultServlet);
                context.addServletMapping("/", name);
            }
        });
        tomcat.startAwait();
    }
}
