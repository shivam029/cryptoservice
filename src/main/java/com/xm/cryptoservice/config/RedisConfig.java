package com.xm.cryptoservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

		return RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(cacheConfig)
				.withCacheConfiguration("allcryptosorteddata", myDefaultCacheConfig(Duration.ofMinutes(1)))
				.withCacheConfiguration("getCryptobyDate", myDefaultCacheConfig(Duration.ofMinutes(1)))
				.withCacheConfiguration("getCryptobyName", myDefaultCacheConfig(Duration.ofMinutes(1)))
				.withCacheConfiguration("getCryptobyDateByAsyncCall", myDefaultCacheConfig(Duration.ofMinutes(1))).build();
		
	}

	private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(duration)
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}
}
