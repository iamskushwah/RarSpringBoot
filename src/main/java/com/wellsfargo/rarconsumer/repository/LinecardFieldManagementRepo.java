package com.wellsfargo.rarconsumer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.Fields;

public interface LinecardFieldManagementRepo extends MongoRepository<Fields, Long> {
    Optional<Fields> findByFieldID(long examinerID);
}
