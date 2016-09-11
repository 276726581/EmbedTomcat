package test;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TimoRD on 2016/7/7.
 */
public class TestServlet extends HttpServlet {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("*********************************************************************");

        HttpRequest httpRequest = new HttpRequest();

        String method = req.getMethod();
        httpRequest.setMethod(method);
        System.out.println(method);

        String url = req.getRequestURL().toString();
        httpRequest.setUrl(url);
        System.out.println(url);

        String protocol = req.getProtocol();
        httpRequest.setProtocol(protocol);
        System.out.println(protocol);

        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = req.getParameter(key);
            parameters.put(key, value);
            System.out.println(String.format("%s = %s", key, value));
        }
        httpRequest.setParameters(parameters);

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = req.getHeader(key);
            headers.put(key, value);
            System.out.println(String.format("%s = %s", key, value));
        }
        httpRequest.setHeaders(headers);

        int size = req.getContentLength();
        if (-1 != size) {
            ServletInputStream in = req.getInputStream();
            byte[] body = new byte[size];
            in.read(body, 0, size);
            httpRequest.setBody(body);
            System.out.println(body);

            System.out.println(httpRequest.toString());
        }

        AsyncContext asyncContext = req.startAsync(req, resp);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    ServletResponse response = asyncContext.getResponse();
                    response.getWriter().println("resp");
                    asyncContext.complete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("*********************************************************************");
    }

    public class HttpRequest {
        private String method;
        private String url;
        private String protocol;
        private Map<String, String> parameters;
        private Map<String, String> headers;
        private byte[] body;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public byte[] getBody() {
            return body;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        public HttpRequest() {
        }

        @Override
        public String toString() {
            return "HttpRequest{" +
                    "method='" + method + '\'' +
                    ", url='" + url + '\'' +
                    ", protocol='" + protocol + '\'' +
                    ", parameters=" + parameters +
                    ", headers=" + headers +
                    ", body='" + body + '\'' +
                    '}';
        }
    }
}
