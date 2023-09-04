package com.xm.cryptoservice.service;

import java.util.List;

import com.xm.cryptoservice.entity.CryptoDataSetEntity;
import com.xm.cryptoservice.model.CryptoResponseDTO;
import com.xm.cryptoservice.model.MinmaxResponseDTO;

/**
 * @author Shivam_Singh
 * 
 */

public interface CryptoappService {

	MinmaxResponseDTO getRequestedCryptoDataByName(String cryptoName);

	String getRequestedCryptoDataByDate(String cryptoDate);

	CryptoResponseDTO getAllCryptoSortByDescending(List<CryptoDataSetEntity> allCryptoDataFromDB);

}
