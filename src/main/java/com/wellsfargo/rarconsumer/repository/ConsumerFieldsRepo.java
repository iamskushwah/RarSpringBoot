package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.wellsfargo.rarconsumer.entity.ConsumerFields;

public interface ConsumerFieldsRepo extends MongoRepository<ConsumerFields, String> {

	Optional<ConsumerFields> findByFieldIDAndCollName(long fieldID,String collName);
	
	Optional<ConsumerFields> findAllByFieldName(String fieldName);
	
	Optional<ConsumerFields> findByFieldID(long fieldId);
	
	ConsumerFields findByFieldName(String fieldName);
	
	Optional<List<ConsumerFields>> findBySectionIDInAndCollNameIn(List<Long> lineCardSections,List<String> collNames);

	List<ConsumerFields> findByCollName(String collName);
	
	List<ConsumerFields> findByFieldIDIn(List<Long> fieldIds);

	
	
}
