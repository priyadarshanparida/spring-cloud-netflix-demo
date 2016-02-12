package org.self.learn.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EXCHANGE_RATE")
public class ExchangeRate {
	
	@Id
	@Column(name="COUNTRY_CODE")
	private String countryCode;
	
	@Column(name="CONVERSION_FACTOR")
	private String conversionFactor;

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(String conversionFactor) {
		this.conversionFactor = conversionFactor;
	}
	
}
