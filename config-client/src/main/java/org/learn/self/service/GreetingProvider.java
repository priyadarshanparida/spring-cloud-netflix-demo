package org.learn.self.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class GreetingProvider {

    @Autowired
    private Environment env;

    public String getGreeting() {
        return env.getProperty("greeting");
    }

}
