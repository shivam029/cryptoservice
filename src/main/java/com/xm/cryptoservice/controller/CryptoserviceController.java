package com.xm.cryptoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.cryptoservice.model.CryptoResponseDTO;
import com.xm.cryptoservice.model.LoadResponseDTO;
import com.xm.cryptoservice.model.MinmaxResponseDTO;
import com.xm.cryptoservice.service.CryptoCsvLoadService;
import com.xm.cryptoservice.service.CryptoappService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/crypto")
public class CryptoserviceController {

	@Autowired
	CryptoappService cryptoappService;

	@Autowired
	CryptoCsvLoadService cryptoCsvLoadService;

	@Operation(summary = "Post all crypto from CSV ", description = "This API Loads all CSV data to mongo database ")
	@PostMapping(value = "/load")
	public LoadResponseDTO loadAllCsvDataToDatabase() {

		return cryptoCsvLoadService.loadAllCsvDataToDatabase();

	}

	@Operation(summary = "Get all crypto names ", description = "Return a descending sorted list of all the cryptos ")
	@GetMapping(value = "/sort/desc")
	public CryptoResponseDTO getAllCryptoSortByDescending() {

		return cryptoappService.getAllCryptoSortByDescending(cryptoCsvLoadService.getAllCryptoDetails());

	}

	@Operation(summary = "Get crypto min-max by crypto name", description = "Return the crypto values for oldest/newest/min/max ")
	@GetMapping(value = "/minmax")
	public MinmaxResponseDTO getRequestedCryptoDataByName(@RequestParam(required = true) String cryptoName) {

		return cryptoappService.getRequestedCryptoDataByName(cryptoName);
	}

	@Operation(summary = "Get crypto by date", description = "Return the crypto with the highest normalized range for a specific day")
	@GetMapping(value = "/normalized")
	public String getRequestedCryptoDataByDate(@RequestParam(required = true) String cryptoDate) {

		return cryptoappService.getRequestedCryptoDataByDate(cryptoDate);

	}

}
