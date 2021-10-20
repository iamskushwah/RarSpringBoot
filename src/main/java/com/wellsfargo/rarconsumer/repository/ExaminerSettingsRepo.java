package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ExaminerSettings;

public interface ExaminerSettingsRepo extends MongoRepository<ExaminerSettings, Long> {

    Optional<ExaminerSettings> findByExaminerID(Long examinerID);
    
    List<ExaminerSettings> findAllByLastExamIDAndExaminerIDIn(Long lastExamID,List<Long> examinerIDs);

}
