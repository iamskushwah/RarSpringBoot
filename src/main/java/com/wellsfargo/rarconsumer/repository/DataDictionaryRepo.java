package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.DataDictionary;

public interface DataDictionaryRepo extends MongoRepository<DataDictionary, String>{

	List<DataDictionary> findByExamIDAndExcelSheetName(long examId,String excelName);
	
	Optional<DataDictionary> findAllByExamIDAndSourceColumnAndExcelSheetName(long examId,String tColumn,String excelSheet);
}
