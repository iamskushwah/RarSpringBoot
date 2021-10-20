package com.wellsfargo.rarconsumer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.LineCard;

public interface LineCardRepo extends MongoRepository<LineCard, Long>{
	List<LineCard>  findAllByExamID(Long examId);
	List<LineCard>  findAllByExamIDAndExamSampleIDIn(Long examId,List<Long> sampleIds);
	List<LineCard>  findAllByExamIDAndExaminerIDIn(Long examId,List<Long> examinerIds);
	List<LineCard>  findAllByExamIDAndExaminerIDAndCompleted(Long examId,Long examinerId,Boolean completed);
	List<LineCard> findAllByExamSampleID(long examSampleID);
	LineCard findFirstByLinecardID(String linecardID);
}
