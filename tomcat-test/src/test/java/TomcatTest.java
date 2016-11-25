import com.timogroup.tomcat.mock.MockMultipartRequest;
import com.timogroup.tomcat.mock.MockRequest;
import com.timogroup.tomcat.mock.MockResponse;
import com.timogroup.tomcat.mock.MockTomcat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by TimoRD on 2016/11/25.
 */
public class TomcatTest {

    private MockTomcat mockTomcat;

    @Before
    public void init() throws ServletException {
        mockTomcat = new MockTomcat();
        mockTomcat.init();
    }

    @Test
    public void login() throws ServletException, IOException {
        MockRequest request = mockTomcat.create();
        MockResponse response = request.setMethod(RequestMethod.GET)
                .setRequestURI("/login")
                .addParameter("username", "admin")
                .addParameter("password", "password")
                .call();
        int status = response.getStatus();
        String body = response.getBodyAsString();
        Assert.assertEquals(200, status);
    }

    @Test
    public void upload() throws ServletException, IOException {
        MockRequest request = mockTomcat.create();
        MockMultipartRequest mockMultipartRequest = new MockMultipartRequest();
        mockMultipartRequest.addFile(new MockMultipartFile("test", new byte[]{0xf, 0xf, 0xf}));
        MockResponse response = request.setMethod(RequestMethod.GET)
                .setRequestURI("/upload")
                .setMultipartRequest(mockMultipartRequest)
                .call();
        int status = response.getStatus();
        String body = response.getBodyAsString();
        Assert.assertEquals(200, status);
    }
}
