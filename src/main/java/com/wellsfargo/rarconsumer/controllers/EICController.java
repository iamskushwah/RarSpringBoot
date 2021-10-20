//package com.wellsfargo.rarconsumer.controllers;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.wellsfargo.rarconsumer.entity.ExamSamples;
//import com.wellsfargo.rarconsumer.request.ConsumerLineCardDeleteRequest;
//import com.wellsfargo.rarconsumer.request.ExamManagementResourceRequestDTO;
//import com.wellsfargo.rarconsumer.request.ExamSamplesRequestDTO;
//import com.wellsfargo.rarconsumer.response.ExamSamplesResponsePageableDTO;
//import com.wellsfargo.rarconsumer.response.JSONStatus;
//import com.wellsfargo.rarconsumer.response.LinecardFieldManagementDTO;
//import com.wellsfargo.rarconsumer.response.LinecardSectionRequestDTO;
//import com.wellsfargo.rarconsumer.response.LinecardSectionResponseDTO;
//import com.wellsfargo.rarconsumer.response.LinecardSectionTemplateResponseDTO;
//import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
//import com.wellsfargo.rarconsumer.service.ExamLinecardService;
//import com.wellsfargo.rarconsumer.service.ExamManagementResourceService;
//import com.wellsfargo.rarconsumer.service.ExamSamplesService;
//import com.wellsfargo.rarconsumer.service.ExamService;
//import com.wellsfargo.rarconsumer.service.ExamWrapupService;
//import com.wellsfargo.rarconsumer.service.LinecardFieldManagementService;
//import com.wellsfargo.rarconsumer.util.ResponseUtility;
//
//@RestController
//@RequestMapping("/eic")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class EICController {
//
//	private static final Logger logger = LoggerFactory.getLogger(EICController.class);
//
//	@Autowired
//	private ExamLinecardService examLinecardService;
//	
//    @Autowired
//    ExamManagementResourceService service;
//
//    @Autowired
//    private ExamSamplesService examSamplesService;
//    
//	@Autowired
//	private ExamService examService;
//    
//    @Autowired
//    private LinecardFieldManagementService linecardFieldManagementService;
//    
//	@Autowired
//	private ExamWrapupService examWrapupService;
//
//	@GetMapping("/v1/lcsections")
//	public ResponseEntity<?> getExamLCSections(@RequestParam(value = "examinerid", required = true) Long examinerID) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getExaminerLCSections(examinerID));
//	}
//
//	@PatchMapping("/v1/lcsections/{sectionid}")
//	public ResponseEntity<?> updateLCSections(@PathVariable long sectionid,
//			@RequestBody LinecardSectionResponseDTO payload) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.updateLCSection(sectionid, payload));
//	}
//	
//
//	@GetMapping("/v1/lastexam")
//	public ResponseEntity<?> getLastExam(@RequestParam(value = "examinerid", required = true) Long examinerID) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getLastExamID(examinerID));
//	}
//	
//	@GetMapping("/v1/instructions")
//	public ResponseEntity<?> getExamInstructions(@RequestParam(value = "examinerid", required = true) Long examinerID) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getExamInstructions(examinerID));
//	}
//	
//	@PatchMapping("/v1/instructions/{examinerID}")
//	public ResponseEntity<?> updateExamInstructions(@PathVariable long examinerID,@RequestBody String instructions) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.updateExamInstructions(examinerID, instructions));
//	}
//	
//	@GetMapping("/v1/examsections")
//	public ResponseEntity<?> getExamSections(@RequestParam(value = "examid", required = true) Long examID) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getLCSectionsByExamID(examID));
//	}
//	
//	@GetMapping("/v1/exammanagementresource")
//    public ResponseEntity<?> getExamManagementResource(@RequestParam(value="examinerID", required = true) long examinerID) {
//        return ResponseEntity.status(200).body(service.getExamManagementResource(examinerID));
//    }
//
//    @PutMapping("/v1/examresource")
//    public ResponseEntity<?> createExamManagementResource(@RequestBody ExamManagementResourceRequestDTO requestDTO,@RequestParam(value="examinerid", required = true) long examinerid){
//        return ResponseEntity.status(200).body(service.createExamManagementResourceService(requestDTO,examinerid));
//    }
//
//    @DeleteMapping("/v1/exammanagementresource")
//    public ResponseEntity<?> deleteExamManagementResource(@RequestParam(value="userExamID", required = true) long userExamID) {
//        return ResponseEntity.status(200).body(service.deleteExamManagementResource(userExamID));
//    }
//
//    @PatchMapping("/v1/examresource")
//    public ResponseEntity<?> updateExamManagementResource(@RequestBody ExamManagementResourceRequestDTO requestDTO){
//        return ResponseEntity.status(200).body(service.updateExamManagementResource(requestDTO));
//    }
//
//    @GetMapping("/v1/exammanagementresource/names")
//    public ResponseEntity<?> getAllExamResourceNames(){
//        return ResponseEntity.status(200).body(service.getAllExamResourceNames());
//    }
//
//    @GetMapping("/v1/exammanagementresource/roles")
//    public ResponseEntity<?> getAllExamResourceRoles(){
//        return ResponseEntity.status(200).body(service.getAllExamResourceRoles());
//    }
//
//
//    @GetMapping("/v1/exammanagementresource/samples")
//    public ResponseEntity<?> getAllExamResourceSamples() {
//        return ResponseEntity.status(200).body(service.getAllExamResourceSamples());
//    }
//    
//    @GetMapping("/v1/examsamples")
//    public ExamSamplesResponsePageableDTO getExamSamples(@RequestParam(value="examId",required=true) long examId,@RequestParam(value="excelSheetName",required=true) String excelSheetName) {
//	
//        return examSamplesService.getAll(examId,excelSheetName);
//    }
//
//    @PutMapping("/v1/examsamples")
//    public ResponseEntity createExamSamples(@RequestBody ExamSamplesRequestDTO examSample) {
//
//        return examSamplesService.saveExamSample(examSample);
//    }
//
//    @DeleteMapping("/v1/examsamples/{id}")
//    public ResponseEntity deleteById(@PathVariable String id) {
//
//        return examSamplesService.deleteExamSample(id);
//    }
//
//    @PostMapping("/v1/examsamples/{id}")
//    public ResponseEntity updateById(@PathVariable String id, @RequestBody ExamSamples examSample) {
//
//        return  examSamplesService.updateExamSample(examSample);
//    }
//    
//    @PatchMapping("/v1/examsamples")
//    public ResponseEntity patchById(@RequestBody ExamSamples examSample) {
//        return  examSamplesService.patchExamSample(examSample);
//    }
//    
//    @GetMapping("/v1/fields")
//    public ResponseEntity<List<LinecardFieldManagementDTO>> getAllFieldsBySection(@RequestParam(value="examid", required = true) long examid,@RequestParam(value="section", required = true) String section) throws RuntimeException {
//        return ResponseEntity.status(200).body(linecardFieldManagementService.getExamFieldsBySection(examid,section));
//    }
//
//    @GetMapping("/v1/examfields")
//    public ResponseEntity<List<LinecardFieldManagementDTO>> getAllFields(@RequestParam(value="examinerid", required = true) long examinerid) throws RuntimeException {
//        return ResponseEntity.status(200).body(linecardFieldManagementService.getAllExamFields(examinerid));
//    }
//
//    @PutMapping("/v1/fields")
//    public ResponseEntity createLinecardField(@RequestBody LinecardFieldManagementDTO requestDTO,@RequestParam(value="examinerid", required = true) long examinerid) {
//    	return ResponseEntity.status(200).body(linecardFieldManagementService.createLinecardField(requestDTO,examinerid));
//    }
//    
//    @PatchMapping("/v1/fields")
//    public ResponseEntity updateLinecardField(@RequestBody LinecardFieldManagementDTO requestDTO) {
//    	return ResponseEntity.status(200).body(linecardFieldManagementService.updateLinecardField(requestDTO));
//    }
//    
//    @DeleteMapping("/v1/fields")
//    public ResponseEntity deleteLinecardField(@RequestParam(value="examinerid", required = true) long examinerid,@RequestParam(value="fieldid", required = true) long fieldid) {
//    	return ResponseEntity.status(200).body(linecardFieldManagementService.deleteLinecardField(examinerid,fieldid));
//    }
//    
//    @GetMapping("/v1/exams")
//	public ResponseEntity<ResponseApiDTO> getExamsByStatus( @RequestParam(value="status", required = true) boolean status)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examService.getExamListByStatus(status));
//	}
//    
//    @GetMapping("/v1/reassignlc")
//	public ResponseEntity<ResponseApiDTO> getLinecardToReassign(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getLinecardToReassign(examinerid));
//	}
//    
//    @GetMapping("/v1/lcwithoutconclusion")
//	public ResponseEntity<ResponseApiDTO> getLinecardWithoutConclusion(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getLCWithoutFinalConclusion(examinerid));
//	}
//    
//    @GetMapping("/v1/lcwithoutcomments")
//	public ResponseEntity<ResponseApiDTO> getLinecardWithoutComments(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getLCWithoutEICComments(examinerid));
//	}
//    
//    @GetMapping("/v1/lceiccompleted")
//	public ResponseEntity<ResponseApiDTO> getLinecardEICCompleted(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getLCExaminerCompleted(examinerid));
//	}
//    
//    @GetMapping("/v1/lcwip")
//	public ResponseEntity<ResponseApiDTO> getWIPLinecard(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getWIPSummary(examinerid));
//	}
//    
//    @GetMapping("/v1/remainingsamples")
//	public ResponseEntity<ResponseApiDTO> getRemainingSamples(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getRemainingSamples(examinerid));
//	}
//    
//    @GetMapping("/v1/incompletesamples")
//	public ResponseEntity<ResponseApiDTO> getIncompleteSamples(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getIncompleteSamples(examinerid));
//	}
//    
//    @DeleteMapping("/v1/wrapupsample")
//    public ResponseEntity<ResponseApiDTO> deleteSample(@RequestParam(value="examinerid", required = true) long examinerid,@RequestParam(value="sampleid", required = true) long sampleid,@RequestParam(value="samplecount", required = true) long samplecount)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.deleteSample(sampleid, samplecount, examinerid));
//	}
//    
//    @DeleteMapping("/v1/clearwiplc")
//    public ResponseEntity<ResponseApiDTO> clearWIPLinecard(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.clearWIPLC(examinerid)); 
//	}
//    
//    @DeleteMapping("/v1/clearmislc")
//    public ResponseEntity<ResponseApiDTO> clearMISAssignedLinecard(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.clearMISAssignedLC(examinerid));
//	}
//    
//    @PatchMapping("/v1/lcsectiontemplate")
//	public ResponseEntity<?> updateLCSections(@RequestBody LinecardSectionTemplateResponseDTO payload,@RequestParam(value="examinerid", required = true) long examinerid) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.updateLCSectionTemplate(payload,examinerid));
//	}
//    
//    @PutMapping("/v1/lcsection")
//	public ResponseEntity<?> createLCSection(@RequestBody LinecardSectionRequestDTO payload,@RequestParam(value="examinerid", required = true) long examinerid) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.createLCSection(payload, examinerid));
//	}
//    
//    
//	@GetMapping("/v1/lcardsections")
//	public ResponseEntity<?> getLCSections(@RequestParam(value="lid", required = true)  String lid) {
//		return ResponseUtility.createSuccessResponse(examLinecardService.getLCSectionsByLinecardID(lid));
//	}
//	
//	@GetMapping("/v1/newlc")
//	public ResponseEntity<?> getNewLinecard(@RequestParam(value="examinerid", required = true) long examinerid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getNewLinecard(examinerid));
//	}
//	
//	@GetMapping("/v1/lcdata")
//	public ResponseEntity<?> getLinecardData(@RequestParam(value="lcid", required = true) String lcid)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.getLinecardData(lcid));
//	}
//	
//	@PatchMapping("/v1/lcdata")
//	public ResponseEntity<?> updateLinecardData(@RequestParam(value="examinerid", required = true) String examinerid,@RequestBody Map<String,Object> payload)   throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examWrapupService.updateLinecardData(examinerid,payload));
//	}
//	
//	@PostMapping("/v1/lcexport")
//	public JSONStatus exportLinecard(@RequestParam(value="examinerid", required = true) String examinerid,@RequestParam(value="lcid", required = true) String lid,@RequestBody String payload)   throws RuntimeException {
//		return examWrapupService.exportLinecardData(examinerid,lid,payload);
//	}
//    
//    @PutMapping("/v1/build/delete")
//	public ResponseEntity<?> deleteBuild(@RequestParam(value="sampleId", required = true) long sampleId, @RequestParam(value="comment", required = true) String comment) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.deleteBuild(sampleId,comment));
//	}
//    
//    
//    @GetMapping("/v1/sample_build")
// 	public ResponseEntity<?> getExamSampleBuild(@RequestParam(value="sampleId", required = true) long sampleId) throws RuntimeException {
// 		return ResponseUtility.createSuccessResponse(examSamplesService.getExamSampleBuild(sampleId));
// 	}
//    
//    
//    @PutMapping("/v1/consumer_linecard/delete")
//	public ResponseEntity<?> deleteConsumerLinecard(@RequestBody ConsumerLineCardDeleteRequest deleteRequest) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.deleteConsumerLinecard(deleteRequest));
//	}
//    
//    @GetMapping("/v1/exam_app_data")
//	public ResponseEntity<?> getExamAppData(@RequestParam(value="examId", required = true) long examId,@RequestParam(value="sampleId", required = true) long sampleId) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.getExamAppData(examId,sampleId));
//	}
//    
//    @PostMapping("/v1/build")
//	public ResponseEntity<?> dataInsertOnBuild(@RequestParam(value="sampleId", required = true) long sampleId,@RequestParam(value="sampleSize", required = true) long sampleSize,@RequestParam(value="updatedBy", required = true) String updatedBy) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.dataInsertOnBuild(sampleId,sampleSize,updatedBy));
//	}
//    
//    @PutMapping("/v1/consumer_linecard/delete_validation")
//	public ResponseEntity<?> consumerLinecardDeleteCheck(@RequestParam(value="linecardId", required = true) String linecardId) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.consumerLinecardDeleteCheck(linecardId));
//	}
//    
//    @PutMapping("/v1/exam_sample/delete_validation")
//	public ResponseEntity<?> examSampleDeleteCheck(@RequestParam(value="sampleId", required = true) long sampleId) throws RuntimeException {
//		return ResponseUtility.createSuccessResponse(examSamplesService.examSampleDeleteCheck(sampleId));
//	}
//}
