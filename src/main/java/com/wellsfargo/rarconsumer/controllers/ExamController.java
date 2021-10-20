package com.wellsfargo.rarconsumer.controllers;

import com.wellsfargo.rarconsumer.common.SortType;
import com.wellsfargo.rarconsumer.request.Pagination;
import com.wellsfargo.rarconsumer.request.RequestFormatter;
import com.wellsfargo.rarconsumer.request.RequestValidator;
import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
import com.wellsfargo.rarconsumer.service.ExamService;
import com.wellsfargo.rarconsumer.util.ResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/exam")
public class ExamController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
	
	@Autowired
	private ExamService service;


	@GetMapping("/v1/exams")
	public ResponseEntity<ResponseApiDTO> getExamList(@RequestParam(value = "sort") String sort,
														@RequestParam(value="page", required = true) int page,
													   @RequestParam(value="size", required = true) int size)   throws RuntimeException {
		Pagination pagination = RequestFormatter.getPagination(page,size,sort);
		if(RequestValidator.validatePagination(pagination)) {
			return ResponseUtility.createSuccessResponse(service.getExamList(pagination));
		}
		return null;
	}

	//	API FOR CHANGE ARCHIVED DATA
	@PutMapping("/v1/exams/archived")
	public ResponseEntity<ResponseApiDTO> updateArchived(@RequestParam (value="exam_id", required=true) int examId, @RequestParam(value="status", required=true) boolean status, @RequestParam(value="linecard_dir_final", required=true) String linecardDirFinal) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateArchived(examId,status,linecardDirFinal));

	}

	//API FOR CHANGE ACTIVE DATA
	@PutMapping("/v1/exams/active")
	public ResponseEntity<ResponseApiDTO> updateActive(@RequestParam (value="exam_id", required=true) int examId, @RequestParam(value="status", required=true) boolean status, @RequestParam(value="comment",required=true) String comment) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateActive(examId,status,comment));
	}

	//	API FOR DELETE EXAM DATA
	@PutMapping("/v1/exams/delete")
	public ResponseEntity<ResponseApiDTO> deleteExam(@RequestParam(value="exam_id", required=true) int examId, @RequestParam(value="status", required=true) boolean status, @RequestParam(value="comment",required=true) String comment) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.deleteExam(examId,status,comment));
	}

//	API FOR DOWNLAOD DATA
	@GetMapping("/v1/exams/download")
	public ResponseEntity<ResponseApiDTO> downloadExams() throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.downloadExams());
	}
	
//	API FOR SEARCH DATA
	@GetMapping("/v1/exams/search")
	public ResponseEntity<ResponseApiDTO> searchExams(@RequestParam(value="fieldName",required=true) String fieldName, @RequestParam(value="searchValue", required =true) String searchValue) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.searchExams(fieldName,searchValue));
	}


}
