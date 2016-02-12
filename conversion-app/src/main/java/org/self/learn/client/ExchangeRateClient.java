package org.self.learn.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("exchange-rate-provider")
public interface ExchangeRateClient {

	/**
	 * Throws start up exception
	 * when @RequestParam value is
	 * not specified i.e.
	 * @RequestParam String countryCode
	 * @param countryCode
	 * @return Conversion Factor
	 */
	@RequestMapping(value="/exchangeRate", method=RequestMethod.GET)
	public String getConversionFactor(@RequestParam("countryCode") String countryCode);
}
