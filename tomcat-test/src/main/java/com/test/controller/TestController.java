package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by TimoRD on 2016/7/6.
 */
@Controller
@RequestMapping
public class TestController {

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
}