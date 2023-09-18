package com.xm.cryptoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Shivam_Singh
 * 
 */
@SpringBootApplication
//@EnableCaching
@EnableMongoRepositories
public class CryptoserviceApplication {
	
	public static void main(String[] args) {
		
	  SpringApplication.run(CryptoserviceApplication.class, args);
		
		}
	}
