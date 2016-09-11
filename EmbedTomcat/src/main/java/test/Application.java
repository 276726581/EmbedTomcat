package test;

import com.timogroup.tomcat.DefaultFactory;
import com.timogroup.tomcat.EmbedTomcat;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        EmbedTomcat tomcat = new EmbedTomcat();
        ListenerConfig contextLoaderListener = DefaultFactory.getDefaultContextLoaderListener("classpath:app.xml");
        tomcat.addListener(contextLoaderListener);
        ServletConfig dispatcherServlet = DefaultFactory.getDefaultDispatcherServlet("classpath:dispatcher-servlet.xml");
        dispatcherServlet.setAsyncSupported(true);
        tomcat.addServlet(dispatcherServlet);

        ServletConfig testServlet = new ServletConfig();
        testServlet.setServletName("testServlet");
        testServlet.setServletClass(TestServlet.class);
        testServlet.setUrlPatterns("/test");
        testServlet.setAsyncSupported(true);
        tomcat.addServlet(testServlet);

        tomcat.setPort(8085);
        tomcat.startAwait();
    }
}
