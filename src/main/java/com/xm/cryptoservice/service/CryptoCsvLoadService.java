package com.xm.cryptoservice.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.xm.cryptoservice.entity.CryptoDataSetEntity;
import com.xm.cryptoservice.model.LoadResponseDTO;

public interface CryptoCsvLoadService {

	LoadResponseDTO loadAllCsvDataToDatabase();

	List<CryptoDataSetEntity> getAllCryptoDetails();

	List<CryptoDataSetEntity> getAllCryptoDetailsByName(String cryptoname);

	String getAllCryptoDetailsByDate(String cryptoDate);

	CompletableFuture<String> getRequestedCryptoDataByDateAsyncCall(String cryptoDate);
}
