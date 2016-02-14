package org.self.learn.controller;

import org.self.learn.dto.HelloMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2ClientController {

	@RequestMapping(value="/oauth2client", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HelloMessage> sayHello() {
		return new ResponseEntity<HelloMessage>(new HelloMessage("Hello World!"), HttpStatus.OK);
	}
}