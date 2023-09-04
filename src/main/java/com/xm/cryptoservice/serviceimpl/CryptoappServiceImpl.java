package com.xm.cryptoservice.serviceimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xm.cryptoservice.csvutility.CsvFileLoader;
import com.xm.cryptoservice.entity.CryptoDataSetEntity;
import com.xm.cryptoservice.exception.CryptoserviceException;
import com.xm.cryptoservice.model.CryptoResponseDTO;
import com.xm.cryptoservice.model.MinmaxResponseDTO;
import com.xm.cryptoservice.service.CryptoCsvLoadService;
import com.xm.cryptoservice.service.CryptoappService;

@Service
public class CryptoappServiceImpl implements CryptoappService {

	private static final Logger logger = LoggerFactory.getLogger(CsvFileLoader.class);

	@Autowired
	CryptoCsvLoadService cryptoCsvLoadService;

	@Cacheable(value = "getCryptobyName")
	public MinmaxResponseDTO getRequestedCryptoDataByName(String cryptoName) {

		MinmaxResponseDTO responseDTOMinmax;
        
		var allCryptodetails = cryptoCsvLoadService.getAllCryptoDetailsByName(cryptoName);
		
		if(allCryptodetails.isEmpty()) {
			throw new CryptoserviceException("The crypto " + cryptoName + " is not Supported");
		}

		try {
			var maxValue = allCryptodetails.stream().mapToDouble(CryptoDataSetEntity::getPrice).max().orElse(0);
			var minValue = allCryptodetails.stream().mapToDouble(CryptoDataSetEntity::getPrice).min().orElse(0);

			Date oldestDate = allCryptodetails.stream().map(CryptoDataSetEntity::getTimestamp).min(Date::compareTo)
					.get();

			var oldestValue = allCryptodetails.stream().filter(x -> x.getTimestamp().equals(oldestDate))
					.mapToDouble(CryptoDataSetEntity::getPrice).findFirst().orElse(0);

			var newestDate = allCryptodetails.stream().map(CryptoDataSetEntity::getTimestamp).max(Date::compareTo)
					.get();

			var newestValue = allCryptodetails.stream().filter(x -> x.getTimestamp().equals(newestDate))
					.mapToDouble(CryptoDataSetEntity::getPrice).findFirst().orElse(0);

			responseDTOMinmax = new MinmaxResponseDTO(oldestValue,newestValue,minValue,maxValue);
			
            logger.info("Crypto Values " + responseDTOMinmax);

		} catch (Exception e) {
			throw new CryptoserviceException("Error while fetching for " + cryptoName );
		}

		return responseDTOMinmax;
	}

	@Override
	@Cacheable(value = "getCryptobyDate")
	public String getRequestedCryptoDataByDate(String cryptoDate) {

		return cryptoCsvLoadService.getAllCryptoDetailsByDate(cryptoDate);

	}

	@Override
	@Cacheable(value = "allcryptosorteddata")
	public CryptoResponseDTO getAllCryptoSortByDescending(List<CryptoDataSetEntity> allCryptoData) {

		List<String> symbolLst = allCryptoData.stream().map(CryptoDataSetEntity::getSymbol).distinct()
				.collect(Collectors.toList());

		var resultMap = new HashMap<String, Double>();
		CryptoResponseDTO responseDTOallCryptoDesc;

		for (String symbol : symbolLst) {

			var maxValue = allCryptoData.stream().filter(x -> x.getSymbol().equals(symbol))
					.mapToDouble(CryptoDataSetEntity::getPrice).max().orElse(0);

			var minValue = allCryptoData.stream().filter(x -> x.getSymbol().equals(symbol))
					.mapToDouble(CryptoDataSetEntity::getPrice).min().orElse(0);

			var normalizedVal = (maxValue - minValue) / minValue;

			resultMap.put(symbol, normalizedVal);
		}

		if(resultMap.isEmpty()) {
			throw new CryptoserviceException("No Crypto data available in the DB");
		}
		
		// Sort the keys based on the values in descending order
		var sortedKeys = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		responseDTOallCryptoDesc = new CryptoResponseDTO(sortedKeys);

		logger.info(" Sorted crypto list " + responseDTOallCryptoDesc.data().toString());

		return responseDTOallCryptoDesc;

	}

}
