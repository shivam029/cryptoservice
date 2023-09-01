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

		MinmaxResponseDTO responseDTOMinmax = new MinmaxResponseDTO();

		List<CryptoDataSetEntity> allCryptodetails = cryptoCsvLoadService.getAllCryptoDetailsByName(cryptoName);

		try {
			double maxValue = allCryptodetails.stream().mapToDouble(CryptoDataSetEntity::getPrice).max().orElse(0);
			double minValue = allCryptodetails.stream().mapToDouble(CryptoDataSetEntity::getPrice).min().orElse(0);

			Date oldestDate = allCryptodetails.stream().map(CryptoDataSetEntity::getTimestamp).min(Date::compareTo)
					.get();

			Double oldestValue = allCryptodetails.stream().filter(x -> x.getTimestamp().equals(oldestDate))
					.mapToDouble(CryptoDataSetEntity::getPrice).findFirst().orElse(0);

			Date newestDate = allCryptodetails.stream().map(CryptoDataSetEntity::getTimestamp).max(Date::compareTo)
					.get();

			Double newestValue = allCryptodetails.stream().filter(x -> x.getTimestamp().equals(newestDate))
					.mapToDouble(CryptoDataSetEntity::getPrice).findFirst().orElse(0);

			responseDTOMinmax.setOldest(oldestValue);
			responseDTOMinmax.setNewest(newestValue);
			responseDTOMinmax.setMin(minValue);
			responseDTOMinmax.setMax(maxValue);

			logger.debug("Info " + responseDTOMinmax);

		} catch (Exception e) {
			throw new CryptoserviceException("This crypto " + cryptoName + " is not Supported");
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

		Map<String, Double> resultMap = new HashMap<>();
		CryptoResponseDTO responseDTOallCryptoDesc = new CryptoResponseDTO();

		for (String symbol : symbolLst) {

			double maxValue = allCryptoData.stream().filter(x -> x.getSymbol().equals(symbol))
					.mapToDouble(CryptoDataSetEntity::getPrice).max().orElse(0);

			double minValue = allCryptoData.stream().filter(x -> x.getSymbol().equals(symbol))
					.mapToDouble(CryptoDataSetEntity::getPrice).min().orElse(0);

			double normalizedVal = (maxValue - minValue) / minValue;

			resultMap.put(symbol, normalizedVal);
		}

		// Sort the keys based on the values in descending order
		List<String> sortedKeys = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		responseDTOallCryptoDesc.setData(sortedKeys);

		logger.debug(" Sorted crypto list " + responseDTOallCryptoDesc.getData().toString());

		return responseDTOallCryptoDesc;

	}

}
