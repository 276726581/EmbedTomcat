package test;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * Created by TimoRD on 2016/7/6.
 */
@Controller
@RequestMapping
public class TestController {

    private Logger logger = Logger.getLogger(TestController.class);

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Callable<String> index(HttpServletRequest request) {
        logger.info(request.getMethod() + " index");

        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Web Application";
            }
        };
    }
}