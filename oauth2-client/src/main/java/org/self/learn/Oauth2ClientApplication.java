package org.self.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;

@SpringBootApplication
@EnableOAuth2Resource
@EnableConfigurationProperties
public class Oauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ClientApplication.class, args);
	}
}
