package com.wellsfargo.rarconsumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.KeyLevelRow;

public interface KeyLevelRowRepo extends MongoRepository<KeyLevelRow, String>{

	KeyLevelRow findByKeyHash(String keyHash);
}
