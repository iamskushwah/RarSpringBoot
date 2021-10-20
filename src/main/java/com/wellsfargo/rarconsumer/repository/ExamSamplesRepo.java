package com.wellsfargo.rarconsumer.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ExamSamples;
import org.springframework.data.domain.Sort;

public interface ExamSamplesRepo extends MongoRepository<ExamSamples, String> {
	List<ExamSamples>  findAllByExamID(Long examId);
	ExamSamples findFirstBysampleID(Long sampleId);
	List<ExamSamples> findAllBySampleIDIn(List<Long> sampleIds);
	List<ExamSamples> findAllBySampleID(Long sampleId);
	
	List<ExamSamples> findAllByExamIDAndDeletedAndLOBCriteria(long examId,boolean status,String excelSheetName,Sort sort);
	
	Optional<ExamSamples> findBySampleID(long sampleId);
	
	
	//Optional<ExamSamples> findTopBySampleIDDesc();
	
}
