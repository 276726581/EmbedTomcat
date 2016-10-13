package test;

import com.timogroup.tomcat.EmbedTomcat;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        EmbedTomcat tomcat = new EmbedTomcat("tomcat", 8050);
        tomcat.enableSpringMVC("classpath:app.xml", "classpath:mvc.xml");
        tomcat.startAwait();
    }
}
