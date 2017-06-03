package com.connoisseur.services.restcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ray Xiao on 4/18/17.
 */

@RestController
public class MainRestController {

    //    @RequestMapping("/")
//    @ResponseBody
//    String home() {
//
//        return "Welcome to Connoisseur Account Service";
//    }

    @Value("${words}")
    String words;

    @RequestMapping("/")
    public @ResponseBody
    String getWord() {
        String[] wordArray = words.split(",");
        int i = (int) Math.round(Math.random() * (wordArray.length - 1));
        return wordArray[i];
    }
}
