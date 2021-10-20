package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ConsumerLinecard;

public interface ConsumerLinecardRepo extends MongoRepository<ConsumerLinecard, String>{

	List<ConsumerLinecard> findByExamSampleID(long id);

	Optional<ConsumerLinecard> findByLinecardID(String linecardId);
	
	List<ConsumerLinecard> findByExamIDAndExamSampleIDNotAndExaminerIDNot(long examId,long sampleId,long examinerId);
	
	List<ConsumerLinecard> findByExamSampleIDAndExaminerIDNot(long sampleId,long examinerId);
	
	Optional<List<ConsumerLinecard>> findByLinecardIDIn(List<String> linecardIds);

	Optional<ConsumerLinecard> findByLinecardIDAndExamIDAndSourceDataFile(String linecardId,long examId,String excelName);

	List<ConsumerLinecard> findByExamSampleIDGreaterThanAndExamID(int greater,long examId);
	
	List<ConsumerLinecard> findByExamSampleIDGreaterThanAndExamIDAndCompleted(int greater,long examId,boolean status);

	List<ConsumerLinecard> findByExamSampleIDGreaterThanAndEicCommentsIsNullAndFinalConclusionNotNullAndCompletedAndExamID(int greater,boolean b, long examId);

	List<ConsumerLinecard> findByExamSampleIDGreaterThanAndFinalConclusionIsNullAndExamID(int greater,long examId);


}
