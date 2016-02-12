package org.self.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@Import(RepositoryRestMvcAutoConfiguration.class)
public class ExchangeRateProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateProviderApplication.class, args);
	}
}
