package com.wellsfargo.rarconsumer.repository;

import com.wellsfargo.rarconsumer.entity.UserExam;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.Max;
import java.util.List;

public interface UserExamRepo extends MongoRepository<UserExam, String> {
	List<UserExam> findAllByUserExamID(long userExamID);
    void deleteByUserExamID(long userExamID);
    void deleteAllByUserExamID(long userExamID);
    List<UserExam> findAll();
    UserExam findByUserExamID(long userExamID);
    List<UserExam> findAllByExamID(long examID);
    
}

