package com.xm.cryptoservice.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.xm.cryptoservice.csvutility.CsvFileLoader;
import com.xm.cryptoservice.entity.CryptoDataSetEntity;
import com.xm.cryptoservice.exception.CryptoserviceException;
import com.xm.cryptoservice.model.LoadResponseDTO;
import com.xm.cryptoservice.repository.CryptoDataSetRepository;
import com.xm.cryptoservice.service.CryptoCsvLoadService;

@Service
public class CryptoCsvLoadServiceImpl implements CryptoCsvLoadService {

	private static final Logger logger = LoggerFactory.getLogger(CryptoCsvLoadServiceImpl.class);

	@Autowired
	CryptoDataSetRepository cryptoDataSetRepository;

	@Override
	public LoadResponseDTO loadAllCsvDataToDatabase() {

		LoadResponseDTO loadResponseDTO = null;

		var allCryptodetails = CsvFileLoader.readCryptoCsvFiles();

		if (allCryptodetails.isEmpty()) {

			loadResponseDTO = new LoadResponseDTO(0, "no records", HttpStatus.ACCEPTED);
		}

		for (Map.Entry<String, List<CryptoDataSetEntity>> datalst : allCryptodetails.entrySet()) {

			try {
				List<CryptoDataSetEntity> lst = cryptoDataSetRepository.saveAll(datalst.getValue());
				logger.info("Crypto " + datalst.getKey() + " Inserted = " + lst.size());
				loadResponseDTO = new LoadResponseDTO(lst.size(), "success", HttpStatus.CREATED);

			} catch (Exception e) {
				loadResponseDTO = new LoadResponseDTO(0, "error", HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Error with Data load from csv to mongodb ", e);
			}

		}

		if (loadResponseDTO.status_msg().equals("error")) {
			
			throw new CryptoserviceException("Issue with data upload from csv files to mongodb ");
		}

		return loadResponseDTO;
	}

	public List<CryptoDataSetEntity> getAllCryptoDetails() {

		var cryptoAllRecords = new ArrayList<CryptoDataSetEntity>();

		cryptoDataSetRepository.findAll().forEach(cryptoAllRecords::add);

		return cryptoAllRecords;

	}

	@Override
	public List<CryptoDataSetEntity> getAllCryptoDetailsByName(String cryptoname) {

		var cryptoAllRecords = new ArrayList<CryptoDataSetEntity>();

		cryptoDataSetRepository.findBySymbolContaining(cryptoname).forEach(cryptoAllRecords::add);

		return cryptoAllRecords;

	}

	@Override
	public String getAllCryptoDetailsByDate(String cryptoDate) {

		var cryptoAllRecords = new ArrayList<CryptoDataSetEntity>();

		var resultMap = new HashMap<String, Double>();

		String highestNormalizedKey = null;

		cryptoDataSetRepository.findAll().forEach(cryptoAllRecords::add);

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

		List<String> symbollst = cryptoAllRecords.stream().map(CryptoDataSetEntity::getSymbol).distinct()
				.collect(Collectors.toList());

		for (String key : symbollst) {

			var maxValue = cryptoAllRecords.stream()
					.filter(x -> formatter.format(x.getTimestamp()).equals(cryptoDate))
					.filter(x -> x.getSymbol().equals(key)).mapToDouble(CryptoDataSetEntity::getPrice).max().orElse(0);

			var minValue = cryptoAllRecords.stream()
					.filter(x -> formatter.format(x.getTimestamp()).equals(cryptoDate))
					.filter(x -> x.getSymbol().equals(key)).mapToDouble(CryptoDataSetEntity::getPrice).min().orElse(0);

			var normalizedVal = (maxValue - minValue) / minValue;

			if (!Double.isNaN(normalizedVal)) {
				resultMap.put(key, normalizedVal);
			}
		}

		if (!resultMap.isEmpty()) {

			highestNormalizedKey = resultMap.entrySet().stream()
					.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).map(Map.Entry::getKey).findAny()
					.get();
		}

		logger.info("Highest Normalized Key is " + highestNormalizedKey);

		if (highestNormalizedKey == null) {

			throw new CryptoserviceException("No data available for the provided date " + cryptoDate);
		}

		return highestNormalizedKey;
	}

}
