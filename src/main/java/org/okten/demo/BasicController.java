package org.okten.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
//@ResponseBody
// @Controller + @ResponseBody = @RestController
@RestController
public class BasicController {

    @GetMapping(value = "/hello", produces = "text/plain")
    public String hello() {
        return "Hello from basic controller";
    }
}
