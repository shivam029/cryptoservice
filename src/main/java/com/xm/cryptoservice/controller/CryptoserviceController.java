package com.xm.cryptoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.cryptoservice.service.CryptoappService;


@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoserviceController {
	
	@Autowired
	CryptoappService cryptoappService;
	
	
	/*
	 *  This API Exposes an endpoint that will return a descending sorted list of all the cryptos,
          comparing the normalized range (i.e. (max-min)/min)
     *  
	 */
	
   
    @GetMapping(value = "/sort/desc",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public Object getAllCryptoSortByDescending() {
		
    	return cryptoappService.getAllCryptoSortByDescending();
    	}
    	
	
	
	/*
	 *  The below API exposes an endpoint that will return the oldest/newest/min/max values for a requested crypto
     *  
	 */
    
	@GetMapping(value = "/minmax",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getRequestedCryptoDataByName(@RequestParam String cryptoName) {
		
		return cryptoappService.getRequestedCryptoDataByName(cryptoName);
	}
	
	/*
	 *  The below API exposes an endpoint that will return the crypto with the highest normalized range for a
          specific day
     *  
	 */
   
	@GetMapping(value = "/normalized",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getRequestedCryptoDataByDate(@RequestParam String cryptoDate) {
		
		return cryptoappService.getRequestedCryptoDataByDate(cryptoDate);
	}
	
	
	
	

}
