# EmbedTomcat
## 嵌入式Tomcat

### 使用方式1--运行war:
java -jar embed-tomcat.jar --war app.war

参数:
``` java
--war: war路径 (默认: app.war)");
--display: Tomcat名称 (默认: tomcat)");
--port: 端口号 (默认: 8080)");
--threads: 线程数 (默认: 200)");
--connections: 连接数 (默认: 10000)");
--encoding: 编码 (默认: utf-8)");
--timeout: 超时 (默认: 30000ms)");
```

### 使用方式2--嵌入Tomcat到程序中, 类似Spring boot(暂不支持jsp):

例子:
``` java
EmbedTomcatContainer tomcat = new EmbedTomcatContainer();
tomcat.setPort(8080);
tomcat.addContextInitializer(new DefaultContextInitializer());
SpringInitializer springInitializer = new SpringInitializer();
springInitializer.setContextConfig("classpath:app.xml");
springInitializer.setDispatcherConfig("classpath:mvc.xml");
tomcat.addServletContextInitializer(springInitializer);
tomcat.startAwait();
```

### Mock
例子:
``` java
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        String result = username + " " + password;
        return result;
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
```
上传文件:
``` java
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "test") MultipartFile file) throws IOException {
        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();
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
```
