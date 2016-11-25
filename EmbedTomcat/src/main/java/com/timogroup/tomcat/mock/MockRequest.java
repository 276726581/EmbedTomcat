package com.timogroup.tomcat.mock;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Created by TimoRD on 2016/11/25.
 */
public class MockRequest {

    private DispatcherServlet dispatcherServlet;
    private MockHttpServletRequest request;

    public MockRequest(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
        request = new MockHttpServletRequest();
    }

    public MockRequest setMultipartRequest(MockMultipartRequest multipartRequest) {
        MockHttpServletRequest multipartReq = multipartRequest.getMockMultipartHttpServletRequest();
        //requestURI
        multipartReq.setRequestURI(request.getRequestURI());
        //headers
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            multipartReq.addHeader(key, value);
        }
        //parameters
        multipartReq.setParameters(request.getParameterMap());
        //body
        InputStream inputStream = null;
        try {
            int length = request.getContentLength();
            if (-1 != length) {
                byte[] bytes = new byte[length];
                inputStream = request.getInputStream();
                inputStream.read(bytes);

                multipartReq.setContent(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        request = multipartReq;

        return this;
    }

    public MockRequest setMethod(RequestMethod method) {
        request.setMethod(method.name());
        return this;
    }

    public MockRequest setMethod(String method) {
        request.setMethod(method);
        return this;
    }

    public MockRequest setRequestURI(String requestURI) {
        request.setRequestURI(requestURI);
        return this;
    }

    public MockRequest addHeader(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public MockRequest addParameter(String key, String value) {
        request.addParameter(key, value);
        return this;
    }

    public MockRequest setBody(byte[] body) {
        request.setContent(body);
        return this;
    }

    public MockRequest setContentType(String contentType) {
        request.setContentType(contentType);
        return this;
    }

    public MockResponse call() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        MockResponse mockResponse = new MockResponse(response);
        return mockResponse;
    }
}
