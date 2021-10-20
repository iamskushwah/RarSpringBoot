package com.wellsfargo.rarconsumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.RowLevelDuplicatesResponse;

public interface RowLevelDuplicatesRepo2 extends MongoRepository<RowLevelDuplicatesResponse, String>{

}
