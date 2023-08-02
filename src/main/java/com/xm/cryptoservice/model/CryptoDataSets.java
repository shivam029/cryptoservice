package com.xm.cryptoservice.model;

import java.util.Date;
/**
 * @author Shivam_Singh
 * 
 */

public class CryptoDataSets {
	
	Date timestamp;
	String symbol;
	Double price;
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
		return "CryptoDataSets [timestamp=" + timestamp + ", symbol=" + symbol + ", price=" + price + "]";
	}
	
	
	

}
