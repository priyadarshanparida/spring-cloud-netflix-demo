package org.self.learn.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class ExchangeRateHystrixClient {
	
	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getConversionFactorFallback")
	public String getConversionFactor(String countryCode) {
		//return restTemplate.getForObject("http://exchange-rate-provider/exchangeRate?countryCode={countryCode}", Float.class, countryCode);
		return restTemplate.getForObject("http://exchange-rate-provider/exchangeRate?countryCode="+countryCode, String.class);
	}
	
	public String getConversionFactorFallback(String countryCode) {
		return "0";
	}
}
