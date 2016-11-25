package com.timogroup.tomcat.mock;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

/**
 * Created by TimoRD on 2016/11/25.
 */
public class MockMultipartRequest {

    private MockMultipartHttpServletRequest request;

    public MockMultipartRequest() {
        this.request = new MockMultipartHttpServletRequest();
    }

    public MockMultipartHttpServletRequest getMockMultipartHttpServletRequest() {
        return request;
    }

    public MockMultipartRequest addFile(MockMultipartFile file) {
        request.addFile(file);
        return this;
    }
}
