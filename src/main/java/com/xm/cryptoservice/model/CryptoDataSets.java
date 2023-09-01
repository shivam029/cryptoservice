package com.xm.cryptoservice.model;

import java.util.Date;

public record CryptoDataSets(Date timestamp, String symbol, Double price) {
}
