package com.test;


import com.timogroup.tomcat.EmbedTomcat;
import com.timogroup.tomcat.config.DefaultContextInitializer;
import com.timogroup.tomcat.config.SpringInitializer;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;

/**
 * Created by TimoRD on 2016/7/6.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        URL url = Application.class.getClassLoader().getResource("view/index.jsp");
        System.out.println(url.toString());
        File root = getDocumentRoot();
        System.out.println(root.getAbsolutePath());

        EmbedTomcat tomcat = new EmbedTomcat();
        tomcat.setDocBase(root.getAbsolutePath());
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

    private static File getDocumentRoot() {
        try {
            CodeSource codeSource = Application.class.getProtectionDomain().getCodeSource();
            URL location = (codeSource == null ? null : codeSource.getLocation());
            if (location == null) {
                return null;
            }
            String path = location.getPath();
            URLConnection connection = location.openConnection();
            if (connection instanceof JarURLConnection) {
                path = ((JarURLConnection) connection).getJarFile().getName();
            }
            if (path.indexOf("!/") != -1) {
                path = path.substring(0, path.indexOf("!/"));
            }
            if (path.indexOf(".jar") != -1) {
                path = path.substring(0, path.lastIndexOf("/"));
            }
            return new File(path);
        } catch (IOException ex) {
            return null;
        }
    }
}
