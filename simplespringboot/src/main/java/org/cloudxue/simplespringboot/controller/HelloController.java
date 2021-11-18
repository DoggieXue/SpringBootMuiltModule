package org.cloudxue.simplespringboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MyController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/11/18 上午11:24
 * @Version 1.0
 **/
@RestController
public class HelloController {
    @RequestMapping("/")
    public String hello() {
        return "hello world";
    }
}
