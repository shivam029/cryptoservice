package com.xm.cryptoservice.csvutility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.xm.cryptoservice.model.CryptoDataSets;

/**
 * @author Shivam_Singh
 * 
 */

 
public class CsvFileRead {
	
	private static final Logger logger = LoggerFactory.getLogger(CsvFileRead.class);


	public static Map<String, List<CryptoDataSets>> readCryptoCsvFiles() {
    
		Map<String, List<CryptoDataSets>> csvDataMap = new HashMap<>();
		S3CSVReader svr = new S3CSVReader();
		csvDataMap = svr.readCryptoCsvFilesFromAWSS3Bucket();
		   
		/* Uncomment and use the below method if we want to read cvs 
		       from Project root folder and not from S3 Bucket */
		//csvDataMap = readCryptoCsvFilesFromProjectRootFolder();
		
		return csvDataMap;
	}
	

	public static Map<String, List<CryptoDataSets>> readCryptoCsvFilesFromProjectRootFolder() {
		
		  // CSV file Path
			String filePath = new File("prices").getAbsolutePath();

			logger.info("Csv filePath is " + filePath);
              List<String> fileList = listFilesUsingJavaIO(filePath);
			logger.info("FileLst " + fileList);

			// Create a map to store the CSV data
			Map<String, List<CryptoDataSets>> csvDataMap = new HashMap<>();

			for (String fileName : fileList) {

				String[] cryptoName = fileName.split("_");

				String fullFilePath = filePath + "\\" + fileName;

				try (CSVReader reader = new CSVReader(new FileReader(fullFilePath))) {
					String[] line;
					int count = 0;

					List<CryptoDataSets> cryptoDatasetList = new ArrayList<CryptoDataSets>();
					while ((line = reader.readNext()) != null) {

						if (count > 0) {

							Date datestamp = new Date(Long.parseLong(line[0]));
							CryptoDataSets rows = new CryptoDataSets();
							rows.setTimestamp(datestamp);
							rows.setSymbol(line[1]);
							rows.setPrice(Double.parseDouble(line[2]));

							cryptoDatasetList.add(rows);

						}
						count++;

					}

					csvDataMap.put(cryptoName[0], cryptoDatasetList);
				} catch (IOException | CsvValidationException e) {
					e.printStackTrace();
				}

			}

			return csvDataMap;

		
	}

	public static List<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toList());
	}

}
