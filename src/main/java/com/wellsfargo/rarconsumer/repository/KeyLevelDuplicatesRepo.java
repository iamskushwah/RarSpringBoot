package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.KeyLevelDuplicates;

public interface KeyLevelDuplicatesRepo extends MongoRepository<KeyLevelDuplicates, String>{

	Page<KeyLevelDuplicates> findByExamIDAndDeleteFlagAndRecordValueAndExcelSheetName(long examId,Pageable pageable,String status,String recValue,String excelName);
	
	List<KeyLevelDuplicates> findAllByExamIDAndDeleteFlagAndRecordValueAndExcelSheetName(int examId,String status,String recValue,String excelName);
	
}
