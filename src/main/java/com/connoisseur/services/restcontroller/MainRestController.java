package com.connoisseur.services.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ray Xiao on 4/18/17.
 */
@RestController

public class MainRestController {


    @RequestMapping("/")
    @ResponseBody
    String home() {

        return "Hello World!";
    }
}
