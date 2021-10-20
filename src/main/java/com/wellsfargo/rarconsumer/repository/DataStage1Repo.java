package com.wellsfargo.rarconsumer.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QPageRequest;

import com.wellsfargo.rarconsumer.entity.DataStage1;

public interface DataStage1Repo extends MongoRepository<DataStage1, Long>{

	Page<DataStage1> findAllByExamIDAndDeleteFlagAndExcelSheetName(int examId,String recValue,String excelName,PageRequest pageable);
}
