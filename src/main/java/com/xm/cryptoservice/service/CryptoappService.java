package com.xm.cryptoservice.service;

import java.util.List;
import java.util.Map;
/**
 * @author Shivam_Singh
 * 
 */


public interface CryptoappService {

	Map<String, Double> getRequestedCryptoDataByName(String cryptoname);

	String getRequestedCryptoDataByDate(String cryptodate);

	List<String> getAllCryptoSortByDescending();
	
}
