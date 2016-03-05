package org.self.learn.controller;

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
	private DiscoveryClient discoveryClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private ExchangeRateClient exchangeRateClient;
	@Autowired
	private ExchangeRateHystrixClient exchangeRateHystrixClient;
	
	enum ServiceInvocationTypes {
		SIMPLE_REST_TEMPLATE(1),
		SIMPLE_REST_TEMPLATE_WITH_DISCOVERY_CLIENT(2),
		RIBBON_ENABLED_REST_TEMPLATE(3),
		RIBBON_LOAD_BALANCER_CLIENT(4),
		FEIGN_CLIENT(5),
		HYSTRIX_ENABLED_CLIENT(6);
		
		private int value;
		
		private ServiceInvocationTypes(int val) {
			this.value = val;
		}
		
		private int value() {
			return this.value;
		}
	}
	
	@RequestMapping(value="/convert", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Float convert(@RequestParam("amt") Float amount, @RequestParam("cc") String countryCode, @RequestParam("sit") Integer serviceInvocationType) {
		Float conversionFactor = 0F;
		
		if(ServiceInvocationTypes.SIMPLE_REST_TEMPLATE.value() == serviceInvocationType.intValue()) {
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject("http://localhost:7070/exchangeRate?countryCode={countryCode}", Float.class, countryCode);
			
		} else if(ServiceInvocationTypes.SIMPLE_REST_TEMPLATE_WITH_DISCOVERY_CLIENT.value() == serviceInvocationType.intValue()) {
			InstanceInfo instance = discoveryClient.getNextServerFromEureka("exchange-rate-provider", false);
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject(instance.getHomePageUrl()+"exchangeRate?countryCode="+countryCode, Float.class);
			
		} else if(ServiceInvocationTypes.RIBBON_ENABLED_REST_TEMPLATE.value() == serviceInvocationType.intValue()) {
			String conversionFactorResponse = restTemplate.getForObject("http://exchange-rate-provider/exchangeRate?countryCode="+countryCode, String.class);
			conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):conversionFactor;
			
		} else if(ServiceInvocationTypes.RIBBON_LOAD_BALANCER_CLIENT.value() == serviceInvocationType.intValue()) {
			ServiceInstance serviceInstance = loadBalancerClient.choose("exchange-rate-provider");
			System.out.println("**************************Provider Server Port: "+serviceInstance.getPort()+"**************************");
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject(serviceInstance.getUri()+"/exchangeRate?countryCode="+countryCode, Float.class);
			
		} else if(ServiceInvocationTypes.FEIGN_CLIENT.value() == serviceInvocationType.intValue()) {
			String conversionFactorResponse = exchangeRateClient.getConversionFactor(countryCode);
			conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):conversionFactor;
			
		} else if(ServiceInvocationTypes.HYSTRIX_ENABLED_CLIENT.value() ==  serviceInvocationType.intValue()) {
			String conversionFactorResponse = exchangeRateHystrixClient.getConversionFactor(countryCode);
			conversionFactor = conversionFactorResponse!=null?Float.parseFloat(conversionFactorResponse):conversionFactor;
			
		}
		
		return conversionFactor*amount;
	}
	
}
