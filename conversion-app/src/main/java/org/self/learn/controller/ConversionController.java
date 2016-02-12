package org.self.learn.controller;

import java.util.HashMap;
import java.util.Map;

import org.self.learn.client.ExchangeRateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
	
	@SuppressWarnings("serial")
	Map<Integer, String> serviceInvocationTypes = new HashMap<Integer, String>() {{
		put(1, "SIMPLE_REST_TEMPLATE");
		put(2, "SIMPLE_REST_TEMPLATE_WITH_DISCOVERY_CLIENT");
		put(3, "RIBBON_ENABLED_REST_TEMPLATE");
		put(4, "RIBBON_LOAD_BALANCER_CLIENT");
		put(5, "FEIGN_CLIENT");
	}};
	
	@RequestMapping(value="/convert", method=RequestMethod.GET)
	public Float convert(@RequestParam("amt") Float amount, @RequestParam("cc") String countryCode, @RequestParam("sit") Integer sit) {
		String conversionFactor = null;
		
		if("SIMPLE_REST_TEMPLATE".equals(serviceInvocationTypes.get(sit))) {
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject("http://localhost:7070/exchangeRate?countryCode="+countryCode, String.class);
			
		} else if("SIMPLE_REST_TEMPLATE_WITH_DISCOVERY_CLIENT".equals(serviceInvocationTypes.get(sit))) {
			InstanceInfo instance = discoveryClient.getNextServerFromEureka("exchange-rate-provider", false);
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject(instance.getHomePageUrl()+"exchangeRate?countryCode="+countryCode, String.class);
			
		} else if("RIBBON_ENABLED_REST_TEMPLATE".equals(serviceInvocationTypes.get(sit))) {
			conversionFactor = restTemplate.getForObject("http://exchange-rate-provider/exchangeRate?countryCode="+countryCode, String.class);
			
		} else if("RIBBON_LOAD_BALANCER_CLIENT".equals(serviceInvocationTypes.get(sit))) {
			ServiceInstance serviceInstance = loadBalancerClient.choose("exchange-rate-provider");
			RestTemplate restTemplate = new RestTemplate();
			conversionFactor = restTemplate.getForObject(serviceInstance.getUri()+"/exchangeRate?countryCode="+countryCode, String.class);
			
		} else if("FEIGN_CLIENT".equals(serviceInvocationTypes.get(sit))) {
			conversionFactor = exchangeRateClient.getConversionFactor(countryCode);
			
		}
		
		return Float.parseFloat(conversionFactor)*amount;
	}
	
}
