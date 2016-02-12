package org.self.learn.repository;

import org.self.learn.domain.ExchangeRate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel="exchangeRate", path="exchangeRate")
public interface ExchangeRestRepository extends PagingAndSortingRepository<ExchangeRate, String> {
	
	@RestResource(path="conversionFactor", rel="conversionFactor")
	ExchangeRate findByCountryCode(@Param("countrycode") String countryCode);
}
