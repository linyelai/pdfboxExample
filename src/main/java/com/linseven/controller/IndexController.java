package com.linseven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 11:16
 */
@RestController
public class IndexController {


    @GetMapping("/index")
    @ResponseBody
    public String index(){
        return "index";
    }
}
