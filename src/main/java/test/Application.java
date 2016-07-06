package test;

import com.timogroup.tomcat.EmbedTomcat;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        EmbedTomcat tomcat = new EmbedTomcat("classpath:app.xml", "classpath:dispatcher-servlet.xml");
        tomcat.setPort(8085);
        tomcat.startAwait();
    }
}
