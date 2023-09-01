package com.xm.cryptoservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xm.cryptoservice.ratelimiting.RateLimitInterceptor;

@Configuration
public class BucketAppConfig implements WebMvcConfigurer {

	private final RateLimitInterceptor interceptor;

	public BucketAppConfig(RateLimitInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}
