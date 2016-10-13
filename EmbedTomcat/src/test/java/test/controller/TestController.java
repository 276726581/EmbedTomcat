package test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TimoRD on 2016/7/6.
 */
@RestController
@RequestMapping
public class TestController {

    @RequestMapping(value = "/")
    public String index() {
        return "Web Application";
    }
}