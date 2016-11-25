package com.timogroup.tomcat.mock;

import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Created by TimoRD on 2016/11/25.
 */
public class MockResponse {

    private MockHttpServletResponse response;

    public MockResponse(MockHttpServletResponse response) {
        this.response = response;
    }

    public MockHttpServletResponse getMockHttpServletResponse() {
        return response;
    }

    public int getStatus() {
        int status = response.getStatus();
        return status;
    }

    public byte[] getBodyAsByteArray() {
        byte[] bytes = response.getContentAsByteArray();
        return bytes;
    }

    public String getBodyAsString() throws UnsupportedEncodingException {
        String content = response.getContentAsString();
        return content;
    }

    public int getContentLength() {
        int length = response.getContentLength();
        return length;
    }

    public long getContentLengthLong() {
        long length = response.getContentLengthLong();
        return length;
    }

    public String getContentType() {
        String contentType = response.getContentType();
        return contentType;
    }

    public Collection<String> getHeaderNames() {
        Collection<String> headerNames = response.getHeaderNames();
        return headerNames;
    }

    public String getHeader(String key) {
        String value = response.getHeader(key);
        return value;
    }

    public String getCharacterEncoding() {
        String encoding = response.getCharacterEncoding();
        return encoding;
    }
}
