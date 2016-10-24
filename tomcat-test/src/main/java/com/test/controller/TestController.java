package com.test.controller;

import com.timogroup.tomcat.context.ApplicationContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
}