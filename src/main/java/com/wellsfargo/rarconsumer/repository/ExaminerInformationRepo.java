package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ExaminerInformation;

import java.util.List;
import java.util.Optional;

public interface ExaminerInformationRepo extends MongoRepository<ExaminerInformation, Long>{

    Optional<ExaminerInformation> findByExaminerID(long eicId);
    
    Optional<List<ExaminerInformation>> findByLastNameIgnoreCaseLikeOrFirstNameIgnoreCaseLike(String lastName,String firstName);
    
    List<ExaminerInformation> findAllByEmployeeGroupIgnoreCaseLikeAndActive(String group,Boolean active);

}
