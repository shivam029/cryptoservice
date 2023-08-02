package com.xm.cryptoservice.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xm.cryptoservice.csvutility.CsvFileRead;
import com.xm.cryptoservice.model.CryptoDataSets;
import com.xm.cryptoservice.service.CryptoappService;


@Service
public class CryptoappServiceImpl implements CryptoappService {
	
	private static final Logger logger = LoggerFactory.getLogger(CsvFileRead.class);


	public Map<String, Double> getRequestedCryptoDataByName(String cryptoname) {
		Map<String, Double> resultMap = new HashMap<>();

		Map<String, List<CryptoDataSets>> allCryptodetails = CsvFileRead.readCryptoCsvFiles();
		boolean flag = true;
		if(!allCryptodetails.isEmpty()) {
		for (Map.Entry<String, List<CryptoDataSets>> datalst : allCryptodetails.entrySet()) {

			
			if (datalst.getKey().equals(cryptoname)) {
				flag = false;
				double maxValue = datalst.getValue().stream().mapToDouble(CryptoDataSets::getPrice).max().orElse(0);
				double minValue = datalst.getValue().stream().mapToDouble(CryptoDataSets::getPrice).min().orElse(0);

				Date oldestDate = datalst.getValue().stream().map(CryptoDataSets::getTimestamp).min(Date::compareTo)
						.get();
				
				logger.info("oldestDate " + oldestDate);

				Double oldestValue = datalst.getValue().stream().filter(x -> x.getTimestamp().equals(oldestDate))
						.mapToDouble(CryptoDataSets::getPrice).findFirst().orElse(0);

				Date newestDate = datalst.getValue().stream().map(CryptoDataSets::getTimestamp).max(Date::compareTo)
						.get();
			
				logger.info("newestDate " + newestDate);

				Double newestValue = datalst.getValue().stream().filter(x -> x.getTimestamp().equals(newestDate))
						.mapToDouble(CryptoDataSets::getPrice).findFirst().orElse(0);

				resultMap.put("OLDEST", oldestValue);
				resultMap.put("NEWEST", newestValue);
				resultMap.put("MIN", minValue);
				resultMap.put("MAX", maxValue);
				
			
			}
		}
		if(flag) {
			resultMap.put("This Crypto is not Supported", 0.0);
			}
		}
		else {
			resultMap.put("There is no data availabe to display", 0.0);
		}


		return resultMap;
	}

	@Override
	public String getRequestedCryptoDataByDate(String cryptodate) {

		Map<String, Double> resultMap = new HashMap<>();
		String highestNormalizedKey = "NA";

		Map<String, List<CryptoDataSets>> allCryptodetails = CsvFileRead.readCryptoCsvFiles();

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

		if(!allCryptodetails.isEmpty()) {
		for (Map.Entry<String, List<CryptoDataSets>> datalst : allCryptodetails.entrySet()) {

			// Uncomment below code if we use x_Val for calculating Normalization
		/*	double x_Val = datalst.getValue().stream()
					.filter(x -> formatter.format(x.getTimestamp()).equals(cryptodate))
					.mapToDouble(CryptoDataSets::getPrice).findAny().orElse(0);
        */
			double maxValue = datalst.getValue().stream()
					.filter(x -> formatter.format(x.getTimestamp()).equals(cryptodate))
					.mapToDouble(CryptoDataSets::getPrice).max().orElse(0);
			//logger.info("maxValue "+maxValue);
			double minValue = datalst.getValue().stream()
					.filter(x -> formatter.format(x.getTimestamp()).equals(cryptodate))
					.mapToDouble(CryptoDataSets::getPrice).min().orElse(0);
			//logger.info("minValue "+minValue);
			
			double normalizedVal = (maxValue - minValue) / minValue;

			if (!Double.isNaN(normalizedVal)) {
				resultMap.put(datalst.getKey(), normalizedVal);
	        }
				
		}
		logger.info("resultMappp "+resultMap);
		
        highestNormalizedKey = resultMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey).findFirst().orElse("No data available for this date");
                
		
		}else {
			
			highestNormalizedKey= "Please check the date - No data available for this date ";
		}
		return highestNormalizedKey;

	}

	@Override
	public  List<String> getAllCryptoSortByDescending() {

		Map<String, Double> resultMap = new HashMap<>();
		
		 Map<String, List<CryptoDataSets>> allCryptodetails = CsvFileRead.readCryptoCsvFiles();

		if(!allCryptodetails.isEmpty()) {
		for (Map.Entry<String, List<CryptoDataSets>> datalst : allCryptodetails.entrySet()) {

			double maxValue = datalst.getValue().stream().mapToDouble(CryptoDataSets::getPrice).max().orElse(0);
			double minValue = datalst.getValue().stream().mapToDouble(CryptoDataSets::getPrice).min().orElse(0);
			double normalizedVal = (maxValue - minValue) / minValue;

			resultMap.put(datalst.getKey(), normalizedVal);
         }
		}
		// Sort the keys based on the values in descending order
		List<String> sortedKeys = resultMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
		

		return sortedKeys;
	}

}
