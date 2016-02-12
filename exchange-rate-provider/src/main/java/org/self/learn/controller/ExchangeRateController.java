package org.self.learn.controller;

import org.self.learn.repository.ExchangeRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
public class ExchangeRateController {
	
	@Autowired
	private ExchangeRestRepository exchangeRestRepo;
	
	@RequestMapping(value="/exchangeRate", method=RequestMethod.GET)
	public String getConversionFactor(@RequestParam String countryCode) {
		
		return exchangeRestRepo.findByCountryCode(countryCode)!=null?
				exchangeRestRepo.findByCountryCode(countryCode).getConversionFactor():null;
	}
}
