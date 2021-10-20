package com.wellsfargo.rarconsumer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ExamFields;

public interface ExamFieldsRepo extends MongoRepository<ExamFields, Long> {

	Optional<ExamFields> findByFieldID(long fieldId);
}
