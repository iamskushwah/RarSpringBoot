package com.wellsfargo.rarconsumer.controllers;

import com.wellsfargo.rarconsumer.common.SortType;
import com.wellsfargo.rarconsumer.entity.AuditColl;
import com.wellsfargo.rarconsumer.entity.AuditCollResponseDTO;
import com.wellsfargo.rarconsumer.entity.AuditCollResponseDTO.AuditCollResponseDTOBuilder;
import com.wellsfargo.rarconsumer.entity.CompleteMapping;
import com.wellsfargo.rarconsumer.entity.ConsumerFields;
import com.wellsfargo.rarconsumer.entity.DataDictionary;
import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.request.KeyLevelDeleteRequest;
import com.wellsfargo.rarconsumer.request.KeyLevelRemoveRequest;
import com.wellsfargo.rarconsumer.request.Pagination;
import com.wellsfargo.rarconsumer.request.RequestFormatter;
import com.wellsfargo.rarconsumer.request.RequestValidator;
import com.wellsfargo.rarconsumer.response.ExamParameterResponse;
import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
import com.wellsfargo.rarconsumer.response.keyLevelDuplicatesPageableDTO;
import com.wellsfargo.rarconsumer.service.ExamService;
import com.wellsfargo.rarconsumer.util.Constants;
import com.wellsfargo.rarconsumer.util.ResponseUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/mis")
public class MisController {
	
	private static final Logger logger = LoggerFactory.getLogger(MisController.class);
	
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
	
//	API FOR EDIT EXAMS
	@GetMapping("/v1/exams/edit")
	public ResponseEntity<ResponseApiDTO> editExams(@RequestParam(value="examId",required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.editExam(examId));
	}
	

	
//	API FOR SAVE EDIT EXAM DATA
	@PutMapping("/v1/exams/save_edit_exam")
	public ResponseEntity<ResponseApiDTO> saveEditExam(@RequestParam(value="examName", required=true) String examName, 
			@RequestParam(value="linecardStagingDir" ,required=true) String linecardStagingDir,
			@RequestParam(value="linecardFinalDir",required=true) String linecardFinalDir,
			@RequestParam(value="examId",required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.saveEditExam(examName,linecardStagingDir,linecardFinalDir,examId));
	}
	
//API FOR GET EXAM SPECIFIC FIELD DATA
	@GetMapping("/v1/exams/exam_field")
	public ResponseEntity<ResponseApiDTO> getExamFieldData(@RequestParam(value="examId", required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getExamFieldData(examId));
	}
	
//	API FOR DELETE EXAM SPECIFIC TABLE DATA
	@PutMapping("/v1/exams/delete_exam_field_data")
	public ResponseEntity<ResponseApiDTO> deleteExamFieldData(@RequestParam(value="examId",required=true) int examId ,@RequestParam(value="fieldId",required=true) int fieldId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.deleteExamFieldData(examId,fieldId));
	}
	
//	API FOR CONSUMER FIELDS
	@GetMapping("/v1/exams/consumer_field")
	public ResponseEntity<ResponseApiDTO> getConsumerFieldData(@RequestParam(value="examId", required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getConsumerFieldData(examId));
	}
	
//	API FOR GET KEY LEVEL DUPLICATES DATA
	@GetMapping("/v1/exams/key_level_duplicates")
	public ResponseEntity<ResponseApiDTO> getKeyLevelData(@RequestParam(value="examId",required=true) long examId,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value="page", required = true) int page,
			@RequestParam(value="size", required = true) int size,
			@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		Pagination pagination = RequestFormatter.getPagination(page,size,sort);
		if(RequestValidator.validatePagination(pagination)) {
			keyLevelDuplicatesPageableDTO data = service.getKeyLevelData(examId,pagination,excelName);
			if(data.getResult().size() == 0 && page == 0) {
				boolean duplicateReviewExist = this.service.checkAlreadyDuplicateReviewed(examId, excelName);
				if(!duplicateReviewExist) {
					AuditColl updateResponse = this.service.updateAuditCollData(examId,Constants.DUPLICATE_REVIEWED,excelName);
					AuditCollResponseDTO auditCollResponse = AuditCollResponseDTO.builder().examStatus(updateResponse.getCurrentStatus()).examID(updateResponse.getExamID()).excelSheetName(updateResponse.getExcelSheetName()).data(new ArrayList<String>()).build(); 
					return ResponseUtility.createSuccessResponse(auditCollResponse);
				}else
					return ResponseUtility.createSuccessResponse(data);
				
			}else {
				return ResponseUtility.createSuccessResponse(data);
			}
		}
		return null;
	}
	
//	API FOR DELETE KEY LEVEL DUPLICATES DATA
	@PutMapping("/v1/exams/key_level_duplicates/delete")
	public ResponseEntity<ResponseApiDTO> deleteKeyLevelData(@RequestParam(value="deletedBy",required=true) String deletedBy,@RequestBody KeyLevelRemoveRequest requestData) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.deleteKeyLevelData(requestData,deletedBy));
	}
	
//	API FOR DOWNLOAD KEY LEVEL DUPLICATES DATA
	@GetMapping("/v1/exams/key_level_duplicates/download")
	public ResponseEntity<ResponseApiDTO> downloadKeyLevelData(@RequestParam(value="examId",required=true) int examId,@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.downloadKeyLevelData(examId,excelName));
	}

//	API FOR GET EXAM SECTION FIELD DATA
	@GetMapping("/v1/exams/exam_section")
	public ResponseEntity<ResponseApiDTO> getExamLinecardSection(@RequestParam(value="examId", required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getExamLinecardSection(examId));
	}
	
//	API FOR GET AUDIT COLL  DATA
	@GetMapping("/v1/exams/audit")
	public ResponseEntity<ResponseApiDTO> getAuditCollData(@RequestParam(value="examId", required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getAuditCollData(examId));
	}
	
//	API FOR UPDATE AUDIT COLL  DATA
	@PutMapping("/v1/exams/audit/update")
	public ResponseEntity<ResponseApiDTO> updateAuditCollData(@RequestParam(value="examId",required=true) int examId, @RequestParam(value="status",required=true) String status,@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateAuditCollData(examId,status,excelName));
	}
	
//	API FOR GET ROW LEVEL DUPLICATES DATA
	@GetMapping("/v1/exams/row_level_duplicates")
	public ResponseEntity<ResponseApiDTO> getRowLevelDuplicatesData(@RequestParam(value="examId",required=true) long examId,@RequestParam(value = "sort") String sort,
			@RequestParam(value="page", required = true) int page,
			@RequestParam(value="size", required = true) int size,
			@RequestParam(value="excelSheetName", required = true) String  excelName) throws RuntimeException {
		Pagination pagination = RequestFormatter.getPagination(page,size,sort);
		if(RequestValidator.validatePagination(pagination)) {
			return ResponseUtility.createSuccessResponse(service.getRowLevelDuplicatesData(examId,pagination,excelName));
		}
		return null;
	}
	
//	API FOR GET ROW LEVEL DUPLICATES DATA
	@GetMapping("/v1/exams/row_level_duplicates/download")
	public ResponseEntity<ResponseApiDTO> downlaodRowLevelDuplicates(@RequestParam(value="examId",required=true) int examId, @RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.downloadRowLevelDuplicates(examId,excelName));
	}
	
//	API FOR GET DATA STAGE1
	@GetMapping("/v1/exams/data_stage1")
	public ResponseEntity<ResponseApiDTO> getDataStage1(@RequestParam(value="examId",required=true) int examId,@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getDataStage1(examId,excelName));
	}
	
//	API FOR GET DATA DICTIONARY
	@GetMapping("/v1/exams/data_dictionary")
	public ResponseEntity<ResponseApiDTO> getDataDictionary(@RequestParam(value="examId",required=true) long examId,@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getDataDictionary(examId,excelName));
	}
	
//	API FOR GET DATA DICTIONARY
//	@PutMapping("/v1/exams/data_dictionary/update")
//	public ResponseEntity<ResponseApiDTO> updateDataDictionary(@RequestBody List<DataDictionary> data ) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(this.service.updateDataDictionary(data));
//	}
	
//	API FOR GET DATA FIELD DATA FROM CONUMEREXAM
	@GetMapping("/v1/exams/consumer_exam/fields")
	public ResponseEntity<ResponseApiDTO> getConsumerField(@RequestParam(value="examId",required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getConsumerField(examId));
	}
	
//	API FOR GET DATA FIELD DATA FROM CONUMEREXAM
	@PostMapping("/v1/exams/db_field_name")
	public ResponseEntity<ResponseApiDTO> addConsumerFieldData(@RequestBody ConsumerFields consumerField,@RequestParam(value="sectionId")int sectionId, @RequestParam(value="examId", required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.addConsumerFieldData(consumerField,sectionId,examId));
	}
	
//	API FOR GET EXAM SECTION  DATA FROM CONUMEREXAM
	@GetMapping("/v1/exams/consumer_exam/section")
	public ResponseEntity<ResponseApiDTO> getExamSectionData(@RequestParam(value="examId",required=true) int examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getExamSectionData(examId));
	}
	
	
//	API FOR COMPLETE MAPPING
	@PutMapping("/v1/exams/complete_mapping")
	public ResponseEntity<ResponseApiDTO> CompleteMapping(@RequestBody CompleteMapping object) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateOnCompleteMapping(object));
	}
	
	
//	API FOR COMPLETE MAPPING ADD NEW EDIT
	@PutMapping("/v1/exams/field_mapping/edit")
	public ResponseEntity<ResponseApiDTO> EditOnAddNewScreen(@RequestParam(value="examId",required=true)long examId,
			@RequestParam(value="fieldId",required=true) long fieldId,
			@RequestParam(value="linecardCaption",required=true) String caption,
			@RequestParam(value="linecardSection",required=true) int section) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.EditOnAddNewScreen(examId,fieldId,caption,section));
	}
	
//	API FOR COMPLETE MAPPING ADD NEW SCREEN DELETE
	@DeleteMapping("/v1/exams/field_mapping/delete")
	public ResponseEntity<ResponseApiDTO> deleteFieldMapping(@RequestParam(value="examId",required=true) long examId, @RequestParam(value="fieldId",required=true) long fieldId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.deleteFieldMapping(examId,fieldId));
	}
	
	
//	API FOR REVIEW AND LOAD DATA SCREEN LOAD DATA TO TARGET BUTTON
	@PostMapping("/v1/exams/review_and_load/update")
	public ResponseEntity<ResponseApiDTO> updateReviewAndLoad(@RequestParam(value="examId",required=true) long examId,@RequestParam(value="excelSheetName",required=true) String excelName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateReviewAndLoad(examId,excelName));
	}
	
	
//	API FOR FIND LOOKUP TYPE
	@GetMapping("/v1/exams/lookup_type")
	public ResponseEntity<ResponseApiDTO> getSystmLookup(@RequestParam(value="lookupTypeId", required=true) int lookupTypeId ) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getSystmLookup(lookupTypeId));
	}
	
	
//	API FOR UPDATE EXAM TYPE IN CONSUMER EXAM
	@PutMapping("/v1/exams/update_exam_type/{examId}")
	public ResponseEntity<ResponseApiDTO> updateExamType(@PathVariable("examId") int examId, @RequestParam(value="lookupTypeId", required=true) int lookupTypeId , @RequestParam(value="examTypeValue", required=true) String value) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateExamType(examId,lookupTypeId,value));
	}
	
//	API FOR GET PROJECT MASTER ID WITH EXAM NAME
	@GetMapping("/v1/exams/project_master")
	public ResponseEntity<ResponseApiDTO> getProjectMasterData() throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getProjectMasterData());
	}
	
//	API FOR GET EIC NAME BY PROJECT ID
	@GetMapping("/v1/exams/get_eic_name")
	public ResponseEntity<ResponseApiDTO> getEicName(@RequestParam(value="projectId",required=true) long projectId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getEicName(projectId));
	}
	
//	API FOR ADD NEW CONSUMER EXAM AND NEW AUDIT COLL DATA
	@PostMapping("/v1/exams/add_new_consumer_exam")
	public ResponseEntity<ResponseApiDTO> addNewConsumerExam(@RequestBody ExamParameterResponse exam) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.addNewConsumerExam(exam));
	}
	
////	API FOR ADD NEW CONSUMER EXAM AND NEW AUDIT COLL DATA
//	@GetMapping("/v1/exams/consumer_exam/combination")
//	public ResponseEntity<ResponseApiDTO> getIdNameTypeCombination() throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(this.service.getIdNameTypeCombination());
//	}
//	
//
////	API FOR GET PROJECT ID AND PROJECTLUNAME AND EIC NAME BY EXAMID
//	@GetMapping("/v1/exams/projectId/eic")
//	public ResponseEntity<ResponseApiDTO> getEicNameByExamId(@RequestParam(value="examId",required=true) int examId) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(this.service.getEicNameByExamId(examId));
//	}
//	
////	API FOR GET PROJECT ID AND PROJECTLUNAME AND EIC NAME BY EXAMID
//	@PostMapping("/v1/exams/audit/add")
//	public ResponseEntity<ResponseApiDTO> addExamInAuditCollection(@RequestParam(value="examId",required=true) int examId, @RequestParam(value="excelSheetName",required=true) String excelName ,@RequestParam(value="keyColumns",required=true) List<String> keyColumns) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(this.service.addExamInAuditCollection(examId,excelName,keyColumns));
//	}
	
//	API FOR ADD NEW CONSUMER EXAM AND NEW AUDIT COLL DATA
	@PutMapping("/v1/exams/exam_name")
	public ResponseEntity<ResponseApiDTO> updateExamName(@RequestParam(value="examId",required=true) long examId, @RequestParam(value="examName",required=true) String examName) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.updateExamName(examId,examName));
	}
	
	
//	API FOR ADD NEW CONSUMER EXAM AND NEW AUDIT COLL DATA
	@PutMapping("/v1/exams/deleted_validation")
	public ResponseEntity<ResponseApiDTO> deleteConsumerExam(@RequestParam(value="examId",required=true) long examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.deleteConsumerExam(examId));
	}
	
	@PutMapping("/v1/exams/add_existing_field")
	public ResponseEntity<ResponseApiDTO> addExistingField(@RequestBody ConsumerFields consumerField,@RequestParam(value="examId",required=true) long examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.addExistingField(consumerField,examId));
	}
	
	@GetMapping("/v1/exams/get_existing_field")
	public ResponseEntity<ResponseApiDTO> getExistingField() throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.getExistingField());
	}
	
//	@GetMapping("/v1/exams/get_existing_field/section")
//	public ResponseEntity<ResponseApiDTO> getExistingFieldSectionDropDown() throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(this.service.getExistingFieldSectionDropDown());
//	}
	
	@GetMapping("/v1/exams/wrapup")
	public ResponseEntity<ResponseApiDTO> wrapUpCondition(@RequestParam(value="examId",required=true) long examId) throws RuntimeException {
		return ResponseUtility.createSuccessResponse(this.service.wrapUpCondition(examId));
	}
	
}
//Could not autowire. No beans of 'SearchBotService' type found. 
// Inspection info:Checks autowiring problems in a bean class.
//
//
// C:\Program Files\MongoDB\Server\5.0\data\
// C:\Program Files\MongoDB\Server\5.0\log\