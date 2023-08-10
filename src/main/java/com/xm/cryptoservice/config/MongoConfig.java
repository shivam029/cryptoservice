package com.xm.cryptoservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
	
	  @Value("${my.mongodb.database}")
	  private String mongodbName;

	  @Value("${my.mongodb.host}")
	  private String mongodbHost;
	  
	  @Value("${my.mongodb.port}")
	  private String mongodbPort;


    @Override
    protected String getDatabaseName() {
        return mongodbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://"+mongodbHost+":"+mongodbPort);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
