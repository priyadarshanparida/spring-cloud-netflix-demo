package org.learn.self.controller;

import org.learn.self.service.GreetingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    private GreetingProvider provider;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String greeter() {
        String greeting = provider.getGreeting();

        return greeting + " World!";
    }

}
