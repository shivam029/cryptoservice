package com.xm.cryptoservice.serviceimpl;

import java.util.ArrayList;
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
import com.xm.cryptoservice.model.CryptoDataSets;
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
	public MinmaxResponseDTO getRequestedCryptoDataByName(String cryptoname) {
		
		MinmaxResponseDTO responseDTOMinmaxList = new MinmaxResponseDTO();

		List<CryptoDataSetEntity> allCryptodetails = cryptoCsvLoadService.getAllCryptoDetailsByName(cryptoname);
		List<CryptoDataSets> cryptoDataSetList = new ArrayList<>();
		
		for(CryptoDataSetEntity cryptoData : allCryptodetails ) {
			CryptoDataSets cryptoDetails = new CryptoDataSets();
			
			cryptoDetails.setPrice(cryptoData.getPrice());
			cryptoDetails.setSymbol(cryptoData.getSymbol());
			cryptoDetails.setTimestamp(cryptoData.getTimestamp());
			
			cryptoDataSetList.add(cryptoDetails);
          }
			
		
                double maxValue = cryptoDataSetList.stream()
						                            .mapToDouble(CryptoDataSets::getPrice)
						                            .max().orElse(0);
				double minValue = cryptoDataSetList.stream()
						                            .mapToDouble(CryptoDataSets::getPrice)
						                            .min().orElse(0);

				Date oldestDate = cryptoDataSetList.stream()
						            .map(CryptoDataSets::getTimestamp)
						            .min(Date::compareTo)
						            .get();
				

				Double oldestValue = cryptoDataSetList.stream()
						                              .filter(x -> x.getTimestamp().equals(oldestDate))
						                              .mapToDouble(CryptoDataSets::getPrice)
						                              .findFirst().orElse(0);

				Date newestDate = cryptoDataSetList.stream()
						                           .map(CryptoDataSets::getTimestamp)
						                           .max(Date::compareTo)
						                           .get();

				Double newestValue = cryptoDataSetList.stream()
						                              .filter(x -> x.getTimestamp().equals(newestDate))
						                              .mapToDouble(CryptoDataSets::getPrice)
						                              .findFirst().orElse(0);

				
		       responseDTOMinmaxList.setOldest(oldestValue);
		       responseDTOMinmaxList.setNewest(newestValue);
		       responseDTOMinmaxList.setMin(minValue);
		       responseDTOMinmaxList.setMax(maxValue);
		       
		       logger.info("Info "+responseDTOMinmaxList);

		      return responseDTOMinmaxList;
	}

	@Override
	@Cacheable(value = "getCryptobyDate")
	public String getRequestedCryptoDataByDate(String cryptodate) {

		String highestNormalizedKey = cryptoCsvLoadService.getAllCryptoDetailsByDate(cryptodate);
		
        return highestNormalizedKey;

	}

	@Override
	@Cacheable(value = "allcryptosorteddata")
	public  CryptoResponseDTO getAllCryptoSortByDescending(List<CryptoDataSetEntity> allCryptoDetaildFromDB) {

		
	    List<String> symbolLst = allCryptoDetaildFromDB.stream()
	    		                                       .map(CryptoDataSetEntity::getSymbol)
	    		                                       .distinct().collect(Collectors.toList());
		
		
		Map<String, Double> resultMap = new HashMap<>();
		CryptoResponseDTO responseDTOallCryptoDesc = new CryptoResponseDTO();
		
	    
         for (String symbol : symbolLst) {

			        double maxValue = allCryptoDetaildFromDB.stream()
					                  .filter(x->x.getSymbol().equals(symbol))
					                  .mapToDouble(CryptoDataSetEntity::getPrice)
					                  .max().orElse(0);
		
			        double minValue = allCryptoDetaildFromDB.stream()
					                  .filter(x->x.getSymbol().equals(symbol))
					                  .mapToDouble(CryptoDataSetEntity::getPrice)
					                  .min().orElse(0);
			
			        double normalizedVal = (maxValue - minValue) / minValue;

		 	    resultMap.put(symbol, normalizedVal);
         }
		
		// Sort the keys based on the values in descending order
		List<String> sortedKeys = resultMap.entrySet()
				                             .stream()
                                             .sorted(Map.Entry.<String, Double>comparingByValue()
                                             .reversed())
                                             .map(Map.Entry::getKey)
                                             .collect(Collectors.toList());
		
		responseDTOallCryptoDesc.setData(sortedKeys);
		
		logger.info(" Sorted crypto list "+responseDTOallCryptoDesc.getData().toString());
		
		return responseDTOallCryptoDesc;
	
	}

	

	

}
