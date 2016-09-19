package org.self.learn.controller;

import java.util.function.Function;

import org.self.learn.client.ExchangeRateClient;
import org.self.learn.client.ExchangeRateHystrixClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;

@RestController
public class ConversionController {

	@Autowired
	private static DiscoveryClient discoveryClient;
	@Autowired
	private static RestTemplate restTemplate;
	@Autowired
	private static LoadBalancerClient loadBalancerClient;
	@Autowired
	private static ExchangeRateClient exchangeRateClient;
	@Autowired
	private static ExchangeRateHystrixClient exchangeRateHystrixClient;
	
	private enum ServiceInvocationType {
		SIMPLE_REST_TEMPLATE(1, countryCode -> {
			RestTemplate restTemplate = new RestTemplate();
			Float conversionFactor = restTemplate.getForObject("http://localhost:7070/exchangeRate?countryCode={countryCode}", Float.class, countryCode);
			return conversionFactor;
		}),
		SIMPLE_REST_TEMPLATE_WITH_DISCOVERY_CLIENT(2, countryCode -> {
			InstanceInfo instance = discoveryClient.getNextServerFromEureka("exchange-rate-provider", false);
			RestTemplate restTemplate = new RestTemplate();
			Float conversionFactor = restTemplate.getForObject(instance.getHomePageUrl()+"exchangeRate?countryCode="+countryCode, Float.class);
			return conversionFactor;
		}),
		RIBBON_ENABLED_REST_TEMPLATE(3, countryCode -> {
			String conversionFactorResponse = restTemplate.getForObject("http://exchange-rate-provider/exchangeRate?countryCode="+countryCode, String.class);
			Float conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):0F;
			return conversionFactor;
		}),
		RIBBON_LOAD_BALANCER_CLIENT(4, countryCode -> {
			ServiceInstance serviceInstance = loadBalancerClient.choose("exchange-rate-provider");
			System.out.println("**************************Provider Server Port: "+serviceInstance.getPort()+"**************************");
			RestTemplate restTemplate = new RestTemplate();
			Float conversionFactor = restTemplate.getForObject(serviceInstance.getUri()+"/exchangeRate?countryCode="+countryCode, Float.class);
			return conversionFactor;
		}),
		FEIGN_CLIENT(5, countryCode -> {
			String conversionFactorResponse = exchangeRateClient.getConversionFactor(countryCode);
			Float conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):0F;
			return conversionFactor;
		}),
		HYSTRIX_ENABLED_CLIENT(6, countryCode -> {
			String conversionFactorResponse = exchangeRateHystrixClient.getConversionFactor(countryCode);
			Float conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):0F;
			return conversionFactor;
		});
		
		private int value;
		private Function<String, Float> handler;
		
		private ServiceInvocationType(int val, Function<String, Float> handler) {
			this.value = val;
			this.handler = handler;
		}
		
		public static ServiceInvocationType map(int value) {
			for(ServiceInvocationType sit: ServiceInvocationType.values()) {
				if(sit.value == value) {
					return sit;
				}
			}
			
			return null;
		}
		
	}
	
	@RequestMapping(value="/convert", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Float convert(@RequestParam("amt") Float amount, @RequestParam("cc") String countryCode, @RequestParam("sit") Integer serviceInvocationType) {
		
		Float conversionFactor = ServiceInvocationType.map(serviceInvocationType)!=null?
				ServiceInvocationType.map(serviceInvocationType).handler.apply(countryCode) : 0F;
		
		return conversionFactor*amount;
	}
	
}
