package com.wellsfargo.rarconsumer.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ConsumerGuestExaminer;

public interface ConsumerGuestExaminerRepo extends MongoRepository<ConsumerGuestExaminer, String>{

	Optional<ConsumerGuestExaminer> findByExaminerIDAndEndDateGreaterThan(long eicId,Date date);

	Optional<ConsumerGuestExaminer> findByExaminerID(long examinerId);
}
