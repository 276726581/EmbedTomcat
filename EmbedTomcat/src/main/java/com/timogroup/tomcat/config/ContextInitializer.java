package com.timogroup.tomcat.config;

import org.apache.catalina.Context;

/**
 * Created by TimoRD on 2016/10/20.
 */
public interface ContextInitializer {

    void onStartup(Context context);
}
