package com.wellsfargo.rarconsumer.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.RowLevelDuplicates;
import com.wellsfargo.rarconsumer.request.Pagination;

public interface RowLevelDuplicatesRepo extends MongoRepository<RowLevelDuplicates, String>{

	Page<RowLevelDuplicates> findByExamIDAndRecordValueAndExcelSheetName(long examId, org.springframework.data.domain.Pageable pageable,String recValue,String excelName);
	
	List<RowLevelDuplicates> findAllByExamIDAndRecordValueAndExcelSheetName(int examId,String recValue,String excelName);
}
