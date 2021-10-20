package com.wellsfargo.rarconsumer.repository;

import com.wellsfargo.rarconsumer.entity.UserExamSample;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserExamSampleRepo extends MongoRepository<UserExamSample, String> {

    List<UserExamSample> findAllByUserExamID(long userExamID);

    @DeleteQuery
    void deleteAllByUserExamID (long userExamID);
}