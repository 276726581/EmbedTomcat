package com.timogroup.tomcat.mock;

import org.springframework.mock.web.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by TimoRD on 2016/11/25.
 */
public class MockTomcat {

    private String contextConfigLocation = "classpath:app.xml";
    private String mvcContextConfigLocation = "classpath:mvc.xml";
    private String encoding = "utf-8";

    private DispatcherServlet dispatcherServlet;

    public MockTomcat() {

    }

    public void setContextConfigLocation(String location) {
        this.contextConfigLocation = location;
    }

    public void setMvcContextConfigLocation(String location) {
        this.mvcContextConfigLocation = location;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void init() throws ServletException {
        if (isEmpty(contextConfigLocation)) {
            throw new IllegalArgumentException("contextConfigLocation");
        }
        if (isEmpty(mvcContextConfigLocation)) {
            throw new IllegalArgumentException("mvcContextConfigLocation");
        }
        if (isEmpty(encoding)) {
            throw new IllegalArgumentException("encoding");
        }

        MockServletContext sc = new MockServletContext();
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, contextConfigLocation);
        ServletContextEvent event = new ServletContextEvent(sc);

        ContextLoaderListener contextLoaderListener = new ContextLoaderListener();
        contextLoaderListener.contextInitialized(event);

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding(encoding);
        encodingFilter.init(new MockFilterConfig(sc));

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation(mvcContextConfigLocation);
        dispatcherServlet.init(new MockServletConfig(sc));
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public MockHttpServletResponse mock(MockHttpServletRequest request) throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        return response;
    }

    public MockHttpServletResponse mock(MockMultipartHttpServletRequest request) throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        return response;
    }

    public MockRequest create() {
        MockRequest request = new MockRequest(dispatcherServlet);
        return request;
    }
}
