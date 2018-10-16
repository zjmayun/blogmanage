package com.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunshine on 2018/9/26.
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @RequestMapping(value = "/test")
    public String hello(){
        return "o112221";
    }
}
