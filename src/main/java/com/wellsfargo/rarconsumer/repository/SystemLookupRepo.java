package com.wellsfargo.rarconsumer.repository;

import com.wellsfargo.rarconsumer.entity.SystemLookup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SystemLookupRepo extends MongoRepository<SystemLookup, String> {

    SystemLookup findByLookupTypeID(long lookupTypeID);

}
