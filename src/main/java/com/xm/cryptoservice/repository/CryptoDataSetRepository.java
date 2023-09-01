package com.xm.cryptoservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.xm.cryptoservice.entity.CryptoDataSetEntity;

@Repository
public interface CryptoDataSetRepository extends MongoRepository<CryptoDataSetEntity, String> {

	List<CryptoDataSetEntity> findBySymbolContaining(String symbol);

}