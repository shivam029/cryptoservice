package com.xm.cryptoservice.csvutility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xm.cryptoservice.entity.CryptoDataSetEntity;

/**
 * @author Shivam_Singh
 * 
 */

 
public class CsvFileLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(CsvFileLoader.class);


	public static Map<String, List<CryptoDataSetEntity>> readCryptoCsvFiles() {
    
		Map<String, List<CryptoDataSetEntity>> csvDataMap = new HashMap<>();
		S3CSVReader svr = new S3CSVReader();
		csvDataMap = svr.readCryptoCsvFilesFromAWSS3Bucket();
		
		logger.info("Total csv records read from csv files is "+csvDataMap.size());
		   
		return csvDataMap;
	}
	
}
