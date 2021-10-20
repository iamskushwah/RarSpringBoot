package com.wellsfargo.rarconsumer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.LookupType;

public interface LookupTypeRepo extends MongoRepository<LookupType, Long> {
	

	Optional<LookupType> findByLookupTypeID(int lookupTypeId);
	
	Optional<LookupType> findByLookupType(String lookupType);
}
