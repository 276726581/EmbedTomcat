package com.test.controller;

import com.timogroup.tomcat.context.ApplicationContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by TimoRD on 2016/7/6.
 */
@Controller
@RequestMapping
public class TestController {

    private static AtomicInteger integer = new AtomicInteger(1);

    @RequestMapping(value = "/")
    @ResponseBody
    public String index() {
        return "Web Application";
    }

    @RequestMapping("/jsp")
    public ModelAndView jsp() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("msg", "Test");
        return mv;
    }

    @RequestMapping("/content")
    @ResponseBody
    public String content() {
        String content = new String(String.valueOf("startSize:" + integer.get()));
        return content;
    }

    @RequestMapping("/refresh")
    public void refresh() {
        ApplicationContextUtil.refreshApplicationContext();
        integer.incrementAndGet();
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        String result = username + " " + password;
        return result;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "test") MultipartFile file) throws IOException {
        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();
    }
}