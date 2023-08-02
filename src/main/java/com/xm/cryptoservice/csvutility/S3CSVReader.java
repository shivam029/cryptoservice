package com.xm.cryptoservice.csvutility;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.xm.cryptoservice.model.CryptoDataSets;
/**
 * @author Shivam_Singh
 * 
 */

public class S3CSVReader {
	
	private static final Logger logger = LoggerFactory.getLogger(CsvFileRead.class);

	static String bucketName = "crypto-demo-s3-new-bucket";
	static String prices_folderPath = "prices/";

	public List<Map<String, String>> getS3Records(String bucket, String key)
			throws IOException, CsvValidationException {
		List<Map<String, String>> records = new ArrayList<>();
		try (CSVReaderHeaderAware reader = getReader(bucket, key)) {
			Map<String, String> values;

			while ((values = reader.readMap()) != null) {
				records.add(values);
			}
			return records;
		}
	}

	private CSVReaderHeaderAware getReader(String bucket, String key) {
		CSVParser parser = new CSVParserBuilder().build();
		S3Object object = getS3().getObject(bucket, key);
		var br = new InputStreamReader(object.getObjectContent());
		return (CSVReaderHeaderAware) new CSVReaderHeaderAwareBuilder(br).withCSVParser(parser).build();
	}

	private AmazonS3 getS3() {
		return AmazonS3ClientBuilder.standard()
				// .withCredentials(new ProfileCredentialsProvider("aws-profile"))
				 .withRegion(Regions.EU_NORTH_1)
				.build();
	}

	public Map<String, List<CryptoDataSets>> readCryptoCsvFilesFromAWSS3Bucket() {

		Map<String, List<CryptoDataSets>> csvDataMap = new HashMap<>();

		// List objects from the specified folder in the S3 bucket
		ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName)
				.withPrefix(prices_folderPath);
		ListObjectsV2Result result = getS3().listObjectsV2(request);
		List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();

		// Print the list of object names
		logger.info("Objects in folder '" + prices_folderPath + "':");

		List<String> fileList = new ArrayList<>();

		for (S3ObjectSummary objectSummary : objectSummaries) {
		
			
             if (!objectSummary.getKey().equals(prices_folderPath) && objectSummary.getKey().contains(".csv")) {
				fileList.add(objectSummary.getKey());
			}
		}

		for (String filePathName : fileList) {
			S3CSVReader reader = new S3CSVReader();
			List<Map<String, String>> records;
			List<CryptoDataSets> cryptoDatasetList = new ArrayList<CryptoDataSets>();
			try {

				records = reader.getS3Records(bucketName, filePathName);

				String str = filePathName.substring(prices_folderPath.length());
		
				String[] cryptoName = str.split("_");

				logger.info("cryptoName " + cryptoName[0]);

				for (Map<String, String> eachRecord : records) {


					Date datestamp = new Date(Long.parseLong(eachRecord.get("timestamp")));
					CryptoDataSets rows = new CryptoDataSets();
					rows.setTimestamp(datestamp);
					rows.setSymbol(eachRecord.get("symbol"));
					rows.setPrice(Double.parseDouble(eachRecord.get("price")));

					cryptoDatasetList.add(rows);

				}
				csvDataMap.put(cryptoName[0], cryptoDatasetList);

				
			} catch (CsvValidationException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}
		return csvDataMap;

	}

}