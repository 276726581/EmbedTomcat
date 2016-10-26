package com.timogroup.tomcat.container;

import com.timogroup.tomcat.util.ProgramArgs;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by TimoRD on 2016/10/26.
 */
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        ProgramArgs programArgs = ProgramArgs.parse(args);
        if (programArgs.hasKey("--help")) {
            showHelp();
            return;
        }

        File war;
        if (!programArgs.hasKey("--war")) {
            war = new File("app.war");
        } else {
            war = new File(programArgs.getArg("--war"));
            if (!isWar(war.getAbsolutePath())) {
                throw new RuntimeException("file not war");
            }
        }
        if (!war.exists()) {
            throw new FileNotFoundException(war.getAbsolutePath());
        }

        WarTomcatContainer tomcat = new WarTomcatContainer(war);
        if (programArgs.hasKey("--display")) {
            String display = getString(programArgs.getArg("--display"), tomcat.getDisplayName());
            tomcat.setDisplayName(display);
        }
        if (programArgs.hasKey("--port")) {
            int port = toInt(programArgs.getArg("--port"), tomcat.getPort());
            tomcat.setPort(port);
        }
        if (programArgs.hasKey("--threads")) {
            int threads = toInt(programArgs.getArg("--threads"), tomcat.getMaxThreads());
            tomcat.setMaxThreads(threads);
        }
        if (programArgs.hasKey("--connections")) {
            int connections = toInt(programArgs.getArg("--connections"), tomcat.getMaxConnections());
            tomcat.setMaxConnections(connections);
        }
        if (programArgs.hasKey("--encoding")) {
            String encoding = getString(programArgs.getArg("--encoding"), tomcat.getEncoding());
            tomcat.setEncoding(encoding);
        }
        if (programArgs.hasKey("--timeout")) {
            int timeout = toInt(programArgs.getArg("--timeout"), tomcat.getConnectionTimeout());
            tomcat.setConnectionTimeout(timeout);
        }

        tomcat.startAwait();
    }

    private static void showHelp() {
        System.out.println("--war: war path (default: app.war)");
        System.out.println("--display: display name (default: tomcat)");
        System.out.println("--port: port (default: 8080)");
        System.out.println("--threads: number of threads (default: 200)");
        System.out.println("--connections: number of connections (default: 10000)");
        System.out.println("--encoding: encoding (default: utf-8)");
        System.out.println("--timeout: timeout (default: 30000ms)");
    }

    private static boolean isWar(String war) {
        if (war == null || war.length() == 0) {
            return false;
        }
        int index = war.lastIndexOf(".war");
        if (-1 == index) {
            return false;
        }

        return true;
    }

    private static String getString(String str, String defaultValue) {
        if (str == null || str.length() == 0) {
            return defaultValue;
        } else {
            return str;
        }
    }

    private static int toInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
}
