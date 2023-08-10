package com.xm.cryptoservice.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cryptodetails")
public class CryptoDataSetEntity {
 
	@Id
	String id;
    Date timestamp;
	String symbol;
	Double price;

  public CryptoDataSetEntity() {

  }
  
  public CryptoDataSetEntity(Date timestamp, String symbol, Double price) {
	super();
	this.timestamp = timestamp;
	this.symbol = symbol;
	this.price = price;
}

public String getId() {
    return id;
  }

  
public Date getTimestamp() {
	return timestamp;
}

public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
}

public String getSymbol() {
	return symbol;
}

public void setSymbol(String symbol) {
	this.symbol = symbol;
}

public Double getPrice() {
	return price;
}

public void setPrice(Double price) {
	this.price = price;
}



@Override
public String toString() {
	return "CryptoDataSetEntity [id=" + id + ",timestamp=" + timestamp + ", symbol=" + symbol + ", price=" + price + "]";
}

  
}