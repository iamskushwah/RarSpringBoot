package com.wellsfargo.rarconsumer.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ExamSampleBuild;

public interface ExamSampleBuildRepo extends MongoRepository<ExamSampleBuild, String> {

	List<ExamSampleBuild> findBySampleID(long id);

}
