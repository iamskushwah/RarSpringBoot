package com.wellsfargo.rarconsumer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExamFields;

public interface ExamRepo extends MongoRepository<Exam, Long>, PagingAndSortingRepository<Exam, Long> {

	Page<Exam> findByActive(Pageable pageable, boolean active);

	List<Exam> findAllByActive(Boolean active);

	Optional<Exam> findByExamID(long examId);
	
	List<Exam> findAllByExamIDAndDeleted(int examID,boolean delete);
	
	List<Exam> findAllByProjectIDAndDeleted(int projectID,boolean delete);
	
	List<Exam> findByExamNameIgnoreCaseLikeAndDeleted(String searchValue,boolean delete);
	
	List<Exam> findByExamTypeIgnoreCaseLikeAndDeleted(String searchValue,boolean delete);
	
	List<Exam> findByProjectIDIn(List<Long> projectIds);
	
	Exam findAllByExamID(int examId);

	Exam findFirstByExamID(long lastExamID);
	
	List<Exam> findByActive(boolean active);
	
	Optional<ExamFields> findByFields(long fieldId);
	
	Optional<Exam> findByExamIDAndArchived(long examId,boolean archived);

	Page<Exam> findByActiveAndDeleted(Pageable pageable, boolean active,boolean deleted);
	
	List<Exam> findByActiveAndDeleted(boolean active,boolean deleted);
	
	List<Exam> findByExamId(long examId);



}
