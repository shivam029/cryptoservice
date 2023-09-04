package com.xm.cryptoservice.service;

import java.util.List;
import java.util.Map;

import com.xm.cryptoservice.entity.CryptoDataSetEntity;
import com.xm.cryptoservice.model.CryptoDataSets;
import com.xm.cryptoservice.model.MinmaxResponseDTO;
import com.xm.cryptoservice.model.CryptoResponseDTO;

/**
 * @author Shivam_Singh
 * 
 */

public interface CryptoappService {

	MinmaxResponseDTO getRequestedCryptoDataByName(String cryptoName);

	String getRequestedCryptoDataByDate(String cryptoDate);

	CryptoResponseDTO getAllCryptoSortByDescending(List<CryptoDataSetEntity> allCryptoDataFromDB);

}
