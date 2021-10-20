package com.wellsfargo.rarconsumer.service;

import com.wellsfargo.rarconsumer.entity.AuditColl;
import com.wellsfargo.rarconsumer.entity.CompleteMapping;
import com.wellsfargo.rarconsumer.entity.ConsumerFields;
import com.wellsfargo.rarconsumer.entity.ConsumerLinecardSection;
import com.wellsfargo.rarconsumer.entity.DataDictionary;
import com.wellsfargo.rarconsumer.entity.DataStage1;
import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExamFields;
import com.wellsfargo.rarconsumer.entity.ExamLinecardSections;
import com.wellsfargo.rarconsumer.entity.ExamSection;
import com.wellsfargo.rarconsumer.entity.KeyLevelDuplicates;
import com.wellsfargo.rarconsumer.entity.LookupType;
import com.wellsfargo.rarconsumer.entity.RowLevelDuplicates;
import com.wellsfargo.rarconsumer.entity.RowLevelDuplicatesResponse;
import com.wellsfargo.rarconsumer.model.ExamResponseDTO;
import com.wellsfargo.rarconsumer.request.KeyLevelDeleteRequest;
import com.wellsfargo.rarconsumer.request.KeyLevelRemoveRequest;
import com.wellsfargo.rarconsumer.request.Pagination;
import com.wellsfargo.rarconsumer.response.CombinationIdNameTypeResponseDTO;
import com.wellsfargo.rarconsumer.response.DataDictionaryResponseDTO;
import com.wellsfargo.rarconsumer.response.ExamFieldsResponse;
import com.wellsfargo.rarconsumer.response.ExamParameterResponse;
import com.wellsfargo.rarconsumer.response.ExamResponsePageableDTO;
import com.wellsfargo.rarconsumer.response.ProjectMasterDataResponseDTO;
import com.wellsfargo.rarconsumer.response.LookupTypeResponseDTO;
import com.wellsfargo.rarconsumer.response.RowLevelDuplicatesPageableDTO;
import com.wellsfargo.rarconsumer.response.WrapUpViewResponse;
import com.wellsfargo.rarconsumer.response.getEicNameByExamIdResponse;
import com.wellsfargo.rarconsumer.response.keyLevelDuplicatesPageableDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Array;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExamService {

	ExamResponsePageableDTO getExamList(Pagination pagination) throws RuntimeException;

	List getExamListByStatus(boolean status) throws RuntimeException;

	public String updateArchived(int examId, boolean status, String comment) throws RuntimeException;

	public String updateActive(int examId, boolean status, String comment) throws RuntimeException;

	public String deleteExam(int examId, boolean status, String comment) throws RuntimeException;
	
	public List<ExamResponseDTO> downloadExams() throws RuntimeException;
	
	public ExamResponsePageableDTO searchExams(String fieldName, String searchValue) throws RuntimeException;
	
	public Exam editExam(int examId) throws RuntimeException;
	
	public String saveEditExam(String examName, String linecardStagingDir, String linecardFinalDir,int examId) throws RuntimeException;

	public List<ExamFields> getExamFieldData(int examId) throws RuntimeException;
	
	public String deleteExamFieldData( int examId ,int fieldId) throws RuntimeException;
	
	public List<ExamFieldsResponse> getConsumerFieldData(int examId) throws RuntimeException;
	
	public keyLevelDuplicatesPageableDTO getKeyLevelData(long examId,Pagination pagination,String excelName) throws RuntimeException;
	
	public String deleteKeyLevelData(KeyLevelRemoveRequest requestData,String deletedBy) throws RuntimeException;
	
	public List<Map<String, String>> downloadKeyLevelData(int examId,String excelName) throws RuntimeException;
	
	public List<ExamLinecardSections> getExamLinecardSection(int examId) throws RuntimeException;
	
	public List<AuditColl> getAuditCollData(int examId) throws RuntimeException;
	
	public AuditColl updateAuditCollData(long examId, String status,String excelName) throws RuntimeException;
	
	public RowLevelDuplicatesPageableDTO getRowLevelDuplicatesData(long examId,Pagination pagination,String excelName) throws RuntimeException;
	
	public List<Map<String,String>> downloadRowLevelDuplicates(int examId,String excelName) throws RuntimeException;
	
	public List<Map<String, String>> getDataStage1(int examId,String excelName) throws RuntimeException;
	
	public List<DataDictionary> getDataDictionary(long examId,String excelName) throws RuntimeException;

//	public String updateDataDictionary(List<DataDictionary> data) throws RuntimeException;

	public List<ConsumerFields> getConsumerField(int examId) throws RuntimeException ;
	
	public String addConsumerFieldData(ConsumerFields consumerField,int sectionId,int examId) throws RuntimeException;
	
	public List<ConsumerLinecardSection> getExamSectionData(int examId) throws RuntimeException;
	
	public String updateOnCompleteMapping(CompleteMapping object) throws RuntimeException;
	
	public String EditOnAddNewScreen(long examId,long fieldId,String caption, int section) throws RuntimeException;
	
	public String deleteFieldMapping(long examId, long fieldId) throws RuntimeException;
	
	public String updateReviewAndLoad(long examId,String excelName) throws RuntimeException;
	
	public boolean checkAlreadyDuplicateReviewed(long examId,String excelName) throws RuntimeException;
	
	public List<LookupTypeResponseDTO> getSystmLookup(int lookupTypeId) throws RuntimeException;

	public String updateExamType(int examId,int lookupTypeId,String value) throws RuntimeException;

	public List<ProjectMasterDataResponseDTO> getProjectMasterData() throws RuntimeException;

	public String getEicName(long projectId) throws RuntimeException;
	
	public String addNewConsumerExam(ExamParameterResponse exam) throws RuntimeException;

//	public List<CombinationIdNameTypeResponseDTO> getIdNameTypeCombination() throws RuntimeException;
//
//	public getEicNameByExamIdResponse getEicNameByExamId(int examId) throws RuntimeException;
//
//	public String addExamInAuditCollection(int examId,String excelName,List<String> keyColumns) throws RuntimeException;

	
	public String updateExamName(long examId,String examName) throws RuntimeException;

	public String deleteConsumerExam(long examId) throws RuntimeException;
	
	public String addExistingField(ConsumerFields consumerFields,long examId) throws RuntimeException;
	
	public List<ConsumerFields> getExistingField() throws RuntimeException;
	
	public WrapUpViewResponse wrapUpCondition(long examId) throws RuntimeException;
	
//	public List<ConsumerLinecardSection> getExistingFieldSectionDropDown() throws RuntimeException;
	
}
