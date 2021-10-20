package com.wellsfargo.rarconsumer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ConsumerExamAppData;

public interface ConsumerExamAppDataRepo extends MongoRepository<ConsumerExamAppData, String>{
	Optional<ConsumerExamAppData> findFirstByLinecardID(String lid);
	Optional<Object> findByLinecardID(String id);
}
