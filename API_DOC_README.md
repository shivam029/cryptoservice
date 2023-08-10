#### Documentation for Code  ####
# Spring boot version 3.1.2 is used to develop the service
# Java 17 version is used
# Opencsv library is used to read the CSV files
# Only .csv file will be read and other file formats will be excluded
# All the given csv files are placed inside my AWS account's S3-bucket 'prices' folder in AWS 
     Bucket name = 'crypto-demo-s3-new-bucket'
# Containerizing of the application is possible
      The Dockerfile is added in root folder and we can generate Images using below commands
         1.Command to generate fresh .jar file 
            mvn clean install
         2.Docker Command to build Image:
            docker build -t crypro-service-1aug-image2 .
         3.Command ro run the Image:
            docker run -p 8080:8080 crypro-service-1aug-image2
# There are 2 methods to read the CSV file as mentioned in readCryptoCsvFiles() 
# inside class CsvFileRead.java
        Method-1 -> readCryptoCsvFilesFromAWSS3Bucket() [ Read csv from S3 bucket ]
        Method-2 -> readCryptoCsvFilesFromProjectRootFolder() [ Read from project folder ]
                      this method is commented and we need to remove the comment 
   
 
# All the 3 Requested endpoints are developed 
# Controller file has all the 3 API's defined such as
       1.Get list of all the cryptos returned in descending sorted
           comparing the normalized range (i.e. (max-min)/min)
           Method name = getAllCryptoSortByDescending()
       2. Get the oldest/newest/min/max values for a requested crypto
           Method name = getRequestedCryptoDataByName(..)
       3. Get the crypto with the highest normalized range for a specific day
           Method name = getRequestedCryptoDataByDate(..)


# List of Endpoints

# API-1 ( This is a Get API , no input params required)
http://localhost:8080/api/v1/crypto/sort/desc
# Sample Response:
["ETH","XRP","DOGE","LTC","BTC"]

# API-2 (This is a Get API , Input param= name of Crypto is passed )
http://localhost:8080/api/v1/crypto/minmax?cryptoName=XRP
# Sample Response:
{
"OLDEST": 0.8298,
"MIN": 0.5616,
"MAX": 0.8458,
"NEWEST": 0.5867
}
# API-2 for Unsupported Crypto name
http://localhost:8080/api/v1/crypto/minmax?cryptoName=DUMMY
# Sample Response:
{
"This Crypto is not Supported": 0.0
}

#API-3 Positive scenario( Input param is date is MM-dd-yyyy format)
http://localhost:8080/api/v1/crypto/normalized?cryptoDate=01-06-2022
# Sample Response :
ETH

#API-3 Negative scenario- ( When the date is not available or date format is not good )
http://localhost:8080/api/v1/crypto/normalized?cryptoDate=01-06-2024
# Sample Response :
No data available for this date

$Swagger Link:
http://localhost:8080/swagger-ui/index.htm
http://localhost:8080/swagger-ui/index.html#/cryptoservice-controller/getRequestedCryptoDataByDate

# Things to consider
1. Documentation for code and api are as mentioned above in this file
2. We can add more .csv files to the prices folder if we want to scale it and add more crypto types,
    just we need to follow the csv file format i.e CRYPTO_Name_values.csv
3. The API returns response only for Supported Cryptos whose csv file  
      is available in the 'prices' folder in the specified format
4. Currently All the csv file is considered for reading as no local cache is used,
     But in a Ideal case we can use a NoSql db or AWS ElastiCache with Redis as local-cache 
     and store the CSV contents, We can also enable a cron or enable a Lambda in AWS 
     which can be triggered when ever new CSV file is added or updated and the trigger event can 
     be used to update the contents of csv to local-cache and the 3-API's we developed can be directed to 
     read from the cache instead of reading from csv every time, this way we can have better performance




