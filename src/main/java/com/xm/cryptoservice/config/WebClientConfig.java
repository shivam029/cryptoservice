package com.xm.cryptoservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${cryptoservice.base.url}")
	private String cryptoserviceBaseUrl;

	@Bean
	public WebClient webClient() {
		return WebClient.builder().baseUrl(cryptoserviceBaseUrl).build();
	}

}
