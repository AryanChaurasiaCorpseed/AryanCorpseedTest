package com.corpseed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@EnableJpaRepositories
//@EnableScheduling
@EnableCaching
@SpringBootApplication
public class CorpseedApplication {
	public static void main(String[] args) {
		SpringApplication.run(CorpseedApplication.class, args);
	}
	
	   @Bean
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }

}
