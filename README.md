#### Documentation for Code  ####
# Spring boot version 3.1.2 is used to develop the service
# Java 17 version is used
# Opencsv library is used to read the CSV files
# Only .csv file will be read and other file formats will be excluded
# Rate Limit is Implemented with 10 requests/ per ( this value can be changed from properties file )
# Caching is Implemented using Spring Boot Data Redis 
# The application uses MongoDb instance running as Container on docker for data persistence
# All the given csv files are placed inside my AWS account's S3-bucket 'prices' folder in AWS 
     Bucket name = 'crypto-demo-s3-new-bucket'
#The Dockerfile is added in root folder and we can generate Images to run instance on containers
# Containerizing of the application is possible
 Total 3 containers are required to be created to Run the application
 1. Container-1 is for Spring-boot application created from the Image
 2. Container-2 is for mongo dbinstance created from the latest mongo Image pulled 
 3. Container-3 is for Redis cache, the Image is pulled for Redis:6.2 version 
 
 Steps to Run the Application:
 Note we need to ensure mongodb and redis containers are UP when we run the Spring boot application
 
 List of Commands in sequence to start all the containers and run the application
 Run all the below commands on terminal and ensure Docker is installed you can skip the commands if redis and mongodb image is already pulled 
 
 1.docker pull redis:6.2
 2.docker pull mongo:latest
 3.mvn clean install -DskipTests
 4.docker build -t crypro-recommendation-service .
 
 Next, Go to the root of the resource folder where we have docker-compose.yml file and run the below command
 
 5.docker-compose up
 
 6.docker ps
 
 Please check the logs here all the 3 containers will be started
 Example:
 C:\Users\Shivam_Singh\Desktop\XM-Interview\cryptoservice>docker ps
CONTAINER ID   IMAGE                           COMMAND                  CREATED          STATUS          PORTS                      NAMES
6b1808d8690d   crypro-recommendation-service   "java -jar app.jar"      16 seconds ago   Up 14 seconds   0.0.0.0:8080->8080/tcp     crypro-recommendation-service
d605fd7cb4f6   mongo:latest                    "docker-entrypoint.s…"   16 seconds ago   Up 14 seconds   0.0.0.0:27017->27017/tcp   mongodb-container-crypto
a3a0608e153e   redis:6.2                       "docker-entrypoint.s…"   16 seconds ago   Up 15 seconds   0.0.0.0:6379->6379/tcp     my-redis-crypto
 
We can test all the end point API's as listed below

Note: we can run 'mvn clean install' command without -DskipTests since it will not fail now as we have 
resolved the dependency of TestCases by creating containers for both mongodb and redis 
 
    
# There is a methods to read the CSV file as mentioned in readCryptoCsvFiles() 
# inside class CsvFileRead.java
         readCryptoCsvFilesFromAWSS3Bucket() [ Read csv from S3 bucket ]
 
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

# API-1 ( This is used to Insert all CSV data to Mongo DB Instance running as container , no input params required)
http://localhost:8080/api/v1/crypto/load
# Sample Response:
{
    "size": 85,
    "status_msg": "success",
    "statuscode": "CREATED"
}

# API-2 ( This is a Get API , no input params required)
http://localhost:8080/api/v1/crypto/sort/desc
# Sample Response:
["ETH","XRP","DOGE","LTC","BTC"]

# API-3 (This is a Get API , Input param= name of Crypto is passed )
http://localhost:8080/api/v1/crypto/minmax?cryptoName=XRP
# Sample Response:
{
"OLDEST": 0.8298,
"MIN": 0.5616,
"MAX": 0.8458,
"NEWEST": 0.5867
}
# API-3 for Unsupported Crypto name
http://localhost:8080/api/v1/crypto/minmax?cryptoName=DUMMY
# Sample Response:
{
"This Crypto is not Supported": 0.0
}

#API-4 Positive scenario( Input param is date is MM-dd-yyyy format)
http://localhost:8080/api/v1/crypto/normalized?cryptoDate=01-06-2022
# Sample Response :
ETH

#API-4 Negative scenario- ( When the date is not available or date format is not good )
http://localhost:8080/api/v1/crypto/normalized?cryptoDate=01-06-2024
# Sample Response :
No data available for this date

$Swagger Link:
http://localhost:8080/swagger-ui/index.htm


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




