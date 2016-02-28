package org.self.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulReverseProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulReverseProxyApplication.class, args);
	}
}
