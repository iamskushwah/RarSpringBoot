package com.wellsfargo.rarconsumer.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.AuditColl;

public interface AuditCollRepo extends MongoRepository<AuditColl, String>{

	Optional<AuditColl> findByExamID(int examId);
	
	List<AuditColl> findAllByExamID(long examId);
	
	AuditColl findByExamIDAndExcelSheetName(long examId,String excelName);

	List<AuditColl> findByExamID(long examId,Sort sort);
	
	Optional<List<AuditColl>> findByExamIDAndCurrentStatus(long examId,String status);

}
