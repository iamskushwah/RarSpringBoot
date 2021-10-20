package com.wellsfargo.rarconsumer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.DataStage3;

public interface DataStage3Repo extends MongoRepository<DataStage3, Long> {

List<DataStage3> findByExamIDAndDeleteFlagAndExcelSheetName(long examId,String recValue,String excelName);
}
