package com.xm.cryptoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.cryptoservice.exception.CustomException;
import com.xm.cryptoservice.model.CryptoResponseDTO;
import com.xm.cryptoservice.model.LoadResponseDTO;
import com.xm.cryptoservice.model.MinmaxResponseDTO;
import com.xm.cryptoservice.service.CryptoCsvLoadService;
import com.xm.cryptoservice.service.CryptoappService;


@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoserviceController {
	
	@Autowired
	CryptoappService cryptoappService;
	
	@Autowired
	CryptoCsvLoadService cryptoCsvLoadService;
	
	/*
	 * 
	 * This API Loads all CSV data to mongo database
	 */
	
	 @GetMapping(value = "/load")
	    @ResponseBody
		public LoadResponseDTO loadAllCsvDataToDatabase() {
	    	  
	    	LoadResponseDTO res = cryptoCsvLoadService.loadAllCsvDataToDatabase();
	    	
	    	if(res.getStatus_msg().equals("error")) {
	    		throw new CustomException("Issue with data upload from csv files to mongodb ");
	    	}
				
	    	 
		  return res;
				  
	}
	
	/*
	 *  This API Exposes an endpoint that will return a descending sorted list of all the cryptos,
     *  
	 */
	
   
    @GetMapping(value = "/sort/desc")
    @ResponseBody
	public CryptoResponseDTO getAllCryptoSortByDescending() {
    	  
    	
		CryptoResponseDTO responseObj = cryptoappService.getAllCryptoSortByDescending(cryptoCsvLoadService.getAllCryptoDetails());
    	
    	if (responseObj.getData().isEmpty()) {
    		
    		throw new CustomException("No Crypto data available to display ");
    	    
    	}
           return responseObj;
    	
    }
    	
	
	
	/*
	 *  The below API exposes an endpoint that will return the oldest/newest/min/max values for a requested crypto
     *  
	 */
    
	@GetMapping(value = "/minmax")
	@ResponseBody
	public MinmaxResponseDTO getRequestedCryptoDataByName(@RequestParam(required = true) String cryptoName) {
		
		 MinmaxResponseDTO responseDTO = cryptoappService.getRequestedCryptoDataByName(cryptoName);
		     
		if (responseDTO.getOldest().isNaN()  ) {
		
			throw new CustomException("This crypto "+cryptoName+" is not Supported");
  	    }
		
		return responseDTO;
  	
	}
	
	/*
	 *  The below API exposes an endpoint that will return the crypto with the highest normalized range for a specific day
     *  
	 */
   
	@GetMapping(value = "/normalized")
	@ResponseBody
	public ResponseEntity<String> getRequestedCryptoDataByDate(@RequestParam(required = true) String cryptoDate) {
		
		 String res = cryptoappService.getRequestedCryptoDataByDate(cryptoDate);
	     
			if (res.equals("null")) {
			
				throw new CustomException("No data available for the provided date "+cryptoDate);
	  	    }
			
			return new ResponseEntity<>(res, HttpStatus.OK);
		
	}
	
	
	
	

}
