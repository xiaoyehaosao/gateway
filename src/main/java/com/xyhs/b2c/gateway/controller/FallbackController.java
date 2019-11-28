package com.xyhs.b2c.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljp
 * @apiNote
 * @date 11:25 2019/11/22
 **/
@RestController
public class FallbackController {

    @RequestMapping(value = "/fallback")
    public String fallback(){
        return "fallback nothing";
    }
}
