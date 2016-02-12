package org.self.learn.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @RequestMapping(value="/user", method= RequestMethod.GET)
    public Object user(Principal user) {
        OAuth2Authentication authentication = (OAuth2Authentication) user;
        Authentication userAuthentication = authentication.getUserAuthentication();
        return userAuthentication.getPrincipal();
    }

}
