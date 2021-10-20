package com.wellsfargo.rarconsumer.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.catalina.mapper.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExaminerInformation;
import com.wellsfargo.rarconsumer.entity.KeyLevelColumns;
import com.wellsfargo.rarconsumer.entity.KeyLevelDuplicates;
import com.wellsfargo.rarconsumer.entity.LookupType;
import com.wellsfargo.rarconsumer.entity.ProjectMaster;
import com.wellsfargo.rarconsumer.entity.RowLevelDuplicates;
import com.wellsfargo.rarconsumer.entity.Stage1Column;
import com.wellsfargo.rarconsumer.entity.DataDictionaryDTO;
import com.wellsfargo.rarconsumer.exception.BadRequestException;
import com.wellsfargo.rarconsumer.model.ExamResponseDTO;
import com.wellsfargo.rarconsumer.repository.ConsumerFieldsRepo;
import com.wellsfargo.rarconsumer.repository.ConsumerGenericTemplateRepository;
import com.wellsfargo.rarconsumer.repository.ExamRepo;
import com.wellsfargo.rarconsumer.repository.ExamSamplesRepo;
import com.mongodb.DBObject;
import com.wellsfargo.rarconsumer.entity.AuditColl;
import com.wellsfargo.rarconsumer.entity.AuditHistory;
import com.wellsfargo.rarconsumer.entity.CompleteMapping;
import com.wellsfargo.rarconsumer.entity.ConsumerFields;
import com.wellsfargo.rarconsumer.entity.ConsumerGenericTemplate;
import com.wellsfargo.rarconsumer.entity.ConsumerGuestExaminer;
import com.wellsfargo.rarconsumer.entity.ConsumerLinecard;
import com.wellsfargo.rarconsumer.entity.ConsumerLinecardSection;
import com.wellsfargo.rarconsumer.entity.DataDictionary;
import com.wellsfargo.rarconsumer.entity.DataStage1;
import com.wellsfargo.rarconsumer.entity.DataStage3;
import com.wellsfargo.rarconsumer.entity.ExamFields;
import com.wellsfargo.rarconsumer.entity.ExamLinecardSections;
import com.wellsfargo.rarconsumer.entity.ExamSampleBuild;
import com.wellsfargo.rarconsumer.entity.ExamSamples;
import com.wellsfargo.rarconsumer.repository.ExaminerInformationRepo;
import com.wellsfargo.rarconsumer.repository.LookupTypeRepo;
import com.wellsfargo.rarconsumer.repository.ProjectMasterRepo;
import com.wellsfargo.rarconsumer.request.KeyLevelDeleteData;
import com.wellsfargo.rarconsumer.request.KeyLevelDeleteRequest;
import com.wellsfargo.rarconsumer.request.KeyLevelRemoveRequest;
import com.wellsfargo.rarconsumer.request.Pagination;
import com.wellsfargo.rarconsumer.request.RequestFormatter;
import com.wellsfargo.rarconsumer.response.CombinationIdNameTypeResponseDTO;
import com.wellsfargo.rarconsumer.response.DataDictionaryResponseDTO;
import com.wellsfargo.rarconsumer.response.ExamFieldsResponse;
import com.wellsfargo.rarconsumer.response.ExamParameterResponse;
import com.wellsfargo.rarconsumer.response.LookupTypeResponseDTO;
//import com.wellsfargo.rarconsumer.response.ExamResponseDTO;
import com.wellsfargo.rarconsumer.response.ExamResponsePageableDTO;
import com.wellsfargo.rarconsumer.response.ProjectMasterDataResponseDTO;
import com.wellsfargo.rarconsumer.response.Message;
import com.wellsfargo.rarconsumer.response.RowLevelDuplicatesPageableDTO;
import com.wellsfargo.rarconsumer.response.WrapUpViewResponse;
import com.wellsfargo.rarconsumer.response.getEicNameByExamIdResponse;
import com.wellsfargo.rarconsumer.response.keyLevelDuplicatesPageableDTO;
import com.wellsfargo.rarconsumer.util.AppUtil;
import com.wellsfargo.rarconsumer.util.Constants;
import com.wellsfargo.rarconsumer.util.FieldIdGenerator;

import springfox.documentation.schema.Model;
import springfox.documentation.swagger2.mappers.ModelMapper;

import com.wellsfargo.rarconsumer.repository.ExamFieldsRepo;
import com.wellsfargo.rarconsumer.repository.KeyLevelDuplicatesRepo;
import com.wellsfargo.rarconsumer.repository.AuditCollRepo;
import com.wellsfargo.rarconsumer.repository.RowLevelDuplicatesRepo;
import com.wellsfargo.rarconsumer.repository.DataStage1Repo;
import com.wellsfargo.rarconsumer.repository.DataDictionaryRepo;
import com.wellsfargo.rarconsumer.repository.RowLevelDuplicatesRepo2;
import com.wellsfargo.rarconsumer.repository.ConsumerLinecardSectionRepo;
import com.wellsfargo.rarconsumer.repository.DataStage3Repo;
import com.wellsfargo.rarconsumer.repository.ConsumerLinecardRepo;
import com.wellsfargo.rarconsumer.repository.ConsumerExamAppDataRepo;
import com.wellsfargo.rarconsumer.repository.ConsumerGuestExaminerRepo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class ExamServiceImpl implements ExamService {

	private static final int CompleteMappingObject = 0;

	private static final int List = 0;

	private final String NO_DATA_FOUND = "No Data Found";

	@Autowired
	private ExamFieldsRepo examFieldRepo;

	@Autowired
	private ExamRepo examRepo;

	@Autowired
	private ProjectMasterRepo proMaster;

	@Autowired
	private ExaminerInformationRepo examInfo;

	@Autowired
	private ConsumerFieldsRepo consumerFieldsRepo;

	@Autowired
	private LookupTypeRepo lookup;

	@Autowired
	private KeyLevelDuplicatesRepo keyLevel;
	
	@Autowired
	private RowLevelDuplicatesRepo rowLevel;
	
	@Autowired
	private AuditCollRepo auditCollRepo;
	
	@Autowired
	private DataStage1Repo dataStage1Repo;
	
	@Autowired
	private DataDictionaryRepo dataDictionaryRepo;
	
	@Autowired
	private RowLevelDuplicatesRepo2 rowLevel2;
	
	
	@Autowired
	private ConsumerLinecardSectionRepo consumerLinecardSectionRepo;
	

	@Autowired
	private DataStage3Repo dataStage3Repo;
	
	@Autowired
	private ConsumerLinecardRepo consumerLinecardRepo;
	
	@Autowired
	private ConsumerExamAppDataRepo consumerExamAppDataRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ConsumerGuestExaminerRepo guestExaminerRepo;
	
	@Autowired
	private ConsumerGenericTemplateRepository consumerGenericTemplateRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ExamSamplesRepo examSampleRepo;
	
	

	public ExamResponsePageableDTO getExamList(Pagination pagination) throws RuntimeException{
		List<ExamResponseDTO> examResponse = new ArrayList<ExamResponseDTO>();

		Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
				Sort.by(pagination.getSort()).descending());
		Page<Exam> data = examRepo.findByActiveAndDeleted(pageable, true,false);

		List<Exam> contents = data.getContent();
		Consumer<Exam> method = (n) -> {
			examResponse.add(setExamResponse(n, getExaminerDetail(n.getProjectID()),getAuditCollStatus(n.getExamID())));
		};
		contents.forEach(method);

		return ExamResponsePageableDTO.builder().result(examResponse).pageable(data.getPageable())
				.totalPages(data.getTotalPages()).totalElements(data.getTotalElements()).build();
	}

	private ExaminerInformation getExaminerDetail(long projectId) throws RuntimeException {
		Optional<ProjectMaster> projectMaster = getProjectMasterById(projectId);
		Long eic = Optional.ofNullable(projectMaster.get().getEicID()).orElse(0L);
		if(projectMaster.isPresent() && eic != 0 ) {
			Optional<ExaminerInformation> examInforamtion = getExaminerInformationById(projectMaster.get().getEicID());
			if(examInforamtion.isPresent()) {
				return examInforamtion.get();
			}
		}
		return null;
	}
	
	private AuditColl getAuditCollStatus(long examId) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		List<AuditColl> auditCollData = auditCollRepo.findByExamID(examId,sort);
		if(auditCollData.size() > 0)
		 return auditCollData.get(0);
		else
		return null;
	}


	private Optional<ProjectMaster> getProjectMasterById(long projectId) throws RuntimeException {
		return  proMaster.findByProjectID(projectId);
	}

	private Optional<ExaminerInformation> getExaminerInformationById(long eidId) throws RuntimeException {
		return examInfo.findByExaminerID(eidId);
	}
	
	private ExamResponseDTO setExamResponse(Exam examData, ExaminerInformation examInfoData,AuditColl auditData) {
		ExamResponseDTO examResponse = ExamResponseDTO.builder().id(examData.getId()).examId(examData.getExamID())
				.projectId(examData.getProjectID()).archived(examData.isArchived()).active(examData.isActive())
				.delete(examData.isDeleted()).examName(examData.getExamName()).examType(examData.getExamType())
				.dataLoaded(examData.isDataLoaded())
				.eicName(examInfoData == null ? null : examInfoData.getFirstName() + " " + examInfoData.getLastName()).build();
				//.status(auditData == null? "":auditData.getCurrentStatus()).build();
		return examResponse;

	}

	@Override
	public List getExamListByStatus(boolean status) throws RuntimeException {
		return examRepo.findAllByActive(status);
	}

	// API FOR CHANGE ARCHIVED VALUE
	@Override
	public String updateArchived(int examId, boolean status, String linecardDirFinal) throws RuntimeException {
		Optional<Exam> examData = examRepo.findByExamID(examId);

		if (!examData.isPresent()) {
			throw new BadRequestException(NO_DATA_FOUND);
		}
		examData.get().setArchived(status);
		examData.get().setLinecardDirFinal(linecardDirFinal);
		examRepo.save(examData.get());
		return Message.BOOLEAN_RESPONSE + status;

	}

//	API FOR CHANGE ACTIVE VALUE

	@Override
	public String updateActive(int examId,boolean status, String comment) throws RuntimeException {
		Optional<Exam> examData = examRepo.findByExamID(examId);

		if(!examData.isPresent()) {
			throw new BadRequestException(NO_DATA_FOUND);
		}
		examData.get().setActive(status);
		examData.get().setActiveComment(comment);
		examRepo.save(examData.get());
		return Message.BOOLEAN_RESPONSE + status;
	}

//	API FOR DELETE EXAM DATA

	@Override
	public String deleteExam(int examId, boolean status, String comment) throws RuntimeException {
		Optional<Exam> examData = examRepo.findByExamID(examId);
		Optional<List<AuditColl>> auditStatus = getStatusFromAuditColl(examId);
		System.out.println(auditStatus);
		
		if(auditStatus.isPresent() && auditStatus.get().size() > 0) {
			throw new BadRequestException("This data cannot be deleted");
		}
		else if(examData.get().isArchived()) {
			throw new BadRequestException("Exam is archived Cannot perform delete operation");
		}
		else {
			examData.get().setDeleted(status);
			examData.get().setDeleteComment(comment);
			examRepo.save(examData.get());
			return Message.BOOLEAN_RESPONSE + status;
		}
	}
	
	public Optional<List<AuditColl>> getStatusFromAuditColl(int examId) {
		Optional<List<AuditColl>> data = auditCollRepo.findByExamIDAndCurrentStatus(examId,"LOADED TO TARGET");
		return data;
	
	}

//	API FOR DOWNLOAD EXAMS 
	@Override
	public List<ExamResponseDTO> downloadExams() throws RuntimeException {
		List<ExamResponseDTO> examResponse = new ArrayList<ExamResponseDTO>();
		List<Exam> data = examRepo.findByActiveAndDeleted(true,false);
		
		List<Exam> contents = data;
		Consumer<Exam> method = (n) -> {
			examResponse.add(setExamResponse(n, getExaminerDetail(n.getProjectID()),getAuditCollStatus(n.getExamID())));
		};
		contents.forEach(method);
		return examResponse;
	}

//	API FOR SEARCH EXAMS
	@Override
	public ExamResponsePageableDTO searchExams(String fieldName, String searchValue) throws RuntimeException {
		List<ExamResponseDTO> examResponse = new ArrayList<ExamResponseDTO>();
		List<Exam> searchResponse = new ArrayList<Exam>();
		Set<Exam> searchExamResultSet = new HashSet<Exam>();
		List<String> convertedPredicateList = Stream.of(fieldName.split(",", -1)).collect(Collectors.toList());
		convertedPredicateList.stream().forEach(predicate -> {
			switch (predicate) {
			case "examId":	
				try {
					searchExamResultSet.addAll(examRepo.findAllByExamIDAndDeleted(Integer.parseInt(searchValue), false));
					break;
				}
				catch(NumberFormatException e){
					System.out.println("This is characters");
				}
				
			case "examName":
				searchExamResultSet.addAll(examRepo.findByExamNameIgnoreCaseLikeAndDeleted(searchValue, false));
				break;
			case "examType":
				searchExamResultSet.addAll(examRepo.findByExamTypeIgnoreCaseLikeAndDeleted(searchValue, false));
				break;
			case "projectId":
				try {
					searchExamResultSet.addAll(examRepo.findAllByProjectIDAndDeleted(Integer.parseInt(searchValue), false));
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("This is characters");
				}
				
			case "eicName":
				searchExamResultSet.addAll(getExamListByEicName(searchValue));
				break;
			}
		});
		Consumer<Exam> method = (n) -> {
			examResponse.add(setExamResponse(n, getExaminerDetail(n.getProjectID()),null));
		};
		searchExamResultSet.forEach(method);
		return ExamResponsePageableDTO.builder().result(examResponse).totalElements((long)examResponse.size()).build();
	}

	private List<Exam> getExamListByEicName(String eicName) {
		 String[] arrOfStr = eicName.split(" ", 2);
		 String firstName = arrOfStr[0];
		 String lastName = arrOfStr[0];
		 if(arrOfStr.length > 1 ) {
			lastName = arrOfStr[1];
		 }
		  
		List<Exam> examList = new ArrayList<Exam>();
		Optional<List<ExaminerInformation>> examInformationList = examInfo.findByLastNameIgnoreCaseLikeOrFirstNameIgnoreCaseLike(lastName,firstName);
		if (examInformationList.isPresent()) {
			List<ExaminerInformation> examInformation = examInformationList.get();
			examInformation.forEach(element->{
				if(element.getExaminerID() !=  null) {
					List<Long> eicIdList = examInformationList.get().stream().map(eicData -> eicData.getExaminerID())
					.collect(Collectors.toList());
					
					Optional<List<ProjectMaster>> projectList = proMaster.findByEicIDIn(eicIdList);
					if (projectList.isPresent()) {
					List<Long> projectIds = projectList.get().stream().map(examdata -> examdata.getProjectID())
						.collect(Collectors.toList());
						examList.addAll(examRepo.findByProjectIDIn(projectIds));
					}											
				}
			});

		
			
		}
		return examList;
	}
	

//	API FOR EDIT EXAM
	@Override
	public Exam editExam(int examId) throws RuntimeException {

		Exam data = examRepo.findAllByExamID(examId);
		long projectId = data.getProjectID();

		Optional<ProjectMaster> projectMaster = proMaster.findByProjectID(projectId);
		long eicId = projectMaster.get().getEicID();

		Optional<ExaminerInformation> examinerData = examInfo.findByExaminerID(eicId);
		String examinerName = examinerData.get().getFirstName() + " " + examinerData.get().getLastName();
		data.setEicName(examinerName);
		System.out.println(examinerName);
		return examRepo.save(data);
	}



//API FOR SAVE EDIT EXAM DATA
	@Override
	public String saveEditExam(String examName, String linecardStagingDir, String linecardFinalDir, int examId)
			throws RuntimeException {
		Optional<Exam> data = examRepo.findByExamID(examId);
		if (!data.isPresent()) {
			throw new BadRequestException(NO_DATA_FOUND);
		}
		data.get().setExamName(examName);
		data.get().setLinecardStagingDir(linecardStagingDir);
		data.get().setLinecardDirFinal(linecardFinalDir);
		examRepo.save(data.get());
		return Message.BOOLEAN_RESPONSE + "Data Save Successfully";
	}

//API FOR GET EXAM SPECIFIC TABLE DATA
	@Override
	public List<ExamFields> getExamFieldData(int examId) throws RuntimeException {
		Exam examData = examRepo.findAllByExamID(examId);
		return examData.getFields();
	}

//API FOR DELETE EXAM SPECIFIC TABLE DATA
	@Override
	public String deleteExamFieldData(int examId, int fieldId) throws RuntimeException {
		Exam examData = examRepo.findAllByExamID(examId);
		examData.getFields().forEach(exam -> {
			if (exam.getFieldID() == fieldId) {
				exam.setDeleted(true);
				examFieldRepo.save(exam);
			}
		});
		examRepo.save(examData);
		return Message.BOOLEAN_RESPONSE + true;
	}

//	API FOR CONSUMER FIELDS
	@Override
	public List<ExamFieldsResponse> getConsumerFieldData(int examId) throws RuntimeException {
		List<ExamFieldsResponse> examFields = new ArrayList<ExamFieldsResponse>();
//		Optional<LookupType> consumerExamLookupData = lookup.findByLookupType(Constants.CONSUMER_EXAM_TYPE);
//		if(consumerExamLookupData.isPresent()) {
//		List<LookupTypeResponseDTO> originationsLookup = consumerExamLookupData.get().getLookupValue().stream().filter(lookupObj -> lookupObj.getValue1().equals(examType))
//				  .collect(Collectors.toList());
//		if(!CollectionUtils.isEmpty(originationsLookup)) {
//			 List<Long> examTypeIds = originationsLookup.stream().map(examTypeObj -> examTypeObj.getLookupID()).collect(Collectors.toList());
//			 Optional<List<ConsumerGenericTemplate>> consumerGenericTemplateList = consumerGenericTemplateRepo.findByExamTypeIDIn(examTypeIds);
//			 if(consumerGenericTemplateList.isPresent()) {
//				 List<Long> genericLineCardSecIds = consumerGenericTemplateList.get().stream().map(genericTemplateObj -> genericTemplateObj.getLinecardSectionID()).collect(Collectors.toList());
//				 Optional<List<ConsumerLinecardSection>> consumerLineCardSections = consumerLinecardSectionRepo.findBySectionIDIn(genericLineCardSecIds);
//				 if(consumerLineCardSections.isPresent()) {
//					 List<Long> lineCardSecIds = consumerLineCardSections.get().stream().map(consumerLineCardSection -> consumerLineCardSection.getSectionID()).collect(Collectors.toList());
//					 List<String> collNames = Arrays.asList("tclDataExamSpecific","tclDataGeneric");
//					 return consumerFieldsRepo.findBySectionIDInAndCollNameIn(lineCardSecIds, collNames).get();
//				 }
//			 }
//		}
//		}
		Optional<Exam> consumerExamData = examRepo.findByExamID(examId);
		if(consumerExamData.isPresent()) {
			List<Long> fieldIds = consumerExamData.get().getFields().stream().map(fieldObj -> fieldObj.getFieldID()).collect(Collectors.toList());
			List<ConsumerFields> consumerFieldsList = consumerFieldsRepo.findByFieldIDIn(fieldIds);
			consumerExamData.get().getFields().forEach(examDataFields ->{
				ExamFieldsResponse examFieldsResponse = ExamFieldsResponse.builder().captionOverride(examDataFields.getCaptionOverride()).
						fieldID(examDataFields.getFieldID()).fieldStatus(examDataFields.getFieldStatus()).linecardIDSecOverride(examDataFields.getLinecardIDSecOverride()).
						reportSecIDOverride(examDataFields.getReportSecIDOverride()).required(examDataFields.isRequired()).
						sortOrder(examDataFields.getSortOrder()).supress(examDataFields.isSupress()).toolTipOverride(examDataFields.getToolTipOverride()).build();
				ConsumerFields consumerField = consumerFieldsList.stream().filter(consumerFieldObj->consumerFieldObj.getFieldID() == examDataFields.getFieldID()).findAny()
						  .orElse(null);
				if(consumerField != null) {
					examFieldsResponse.setFieldName(consumerField.getFieldName());
				}
				examFields.add(examFieldsResponse);
			});
		}
		return examFields;
	}

// API FOR GET KEY LEVEL DUPLICATES DATA
	@Override
	public keyLevelDuplicatesPageableDTO getKeyLevelData(long examId,Pagination pagination,String excelName) throws RuntimeException {
		List<ExamResponseDTO> examResponse = new ArrayList<ExamResponseDTO>();
		Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
				Sort.by(pagination.getSort()).descending());
		
		Page<KeyLevelDuplicates> data = keyLevel.findByExamIDAndDeleteFlagAndRecordValueAndExcelSheetName(examId,pageable,"N","KEY-DUP",excelName);
		List<Map<String,String>> examResponseDTO = getKeyLevelDuplicatesResponse(data.getContent());
		return keyLevelDuplicatesPageableDTO.builder().result(examResponseDTO).pageable(data.getPageable())
				.totalPages(data.getTotalPages()).totalElements(data.getTotalElements()).build();
	}
	
	public List<Map<String, String>> getKeyLevelDuplicatesResponse(List<KeyLevelDuplicates> data) {
		List<Map<String, String>> examResponse = new ArrayList<Map<String, String>>();
		data.forEach(element->{
			Map<String, String> ExamResponseMap = new HashMap<>();
			ExamResponseMap.put("id", element.getId());
			ExamResponseMap.put("exam_id", Long.toString(element.getExamID()));
			ExamResponseMap.put("key_hash", element.getKeyHash());
			ExamResponseMap.put("record_value", element.getRecordValue());
			ExamResponseMap.put("excel_sheet_name", element.getExcelSheetName());
			List<KeyLevelColumns> columnValue = element.getData();
			columnValue.forEach(colValue-> {
				ExamResponseMap.put(colValue.getFname(), colValue.getFvalue());
//				ExamResponseMap.put("keyHash", element.getKeyHash());
			});
			examResponse.add(ExamResponseMap);
		});
		return examResponse;
	}

	// API FOR DELETE KEY LEVEL DUPLICATES DATA
	@Override
	public String deleteKeyLevelData(KeyLevelRemoveRequest requestObj,String deletedBy) throws RuntimeException {
		KeyLevelDeleteRequest requestData = getDeleteDataFromRequest(requestObj);
		List<Map<String, String>> KeyLevelDataList = getKeyLevelDuplicatesResponse(keyLevel.findAllByExamIDAndDeleteFlagAndRecordValueAndExcelSheetName(requestData.getExamID(),"N","KEY-DUP",requestData.getExcelSheetName()));
		//iterate request data obj
		requestData.getKeyDuplicateData().forEach(keyLevelDataObj ->{
			keyLevelDataObj.setCanDelete(false);
			List<Map<String, String>> filteredList= new ArrayList<Map<String, String>>();
			filteredList.addAll(KeyLevelDataList);
			keyLevelDataObj.getKeyColumns().entrySet().forEach(keyColumn ->{
				List<Map<String, String>> selectedKeyDups = new ArrayList<Map<String, String>>();
				filteredList.forEach(filterObj ->{
					if(filterObj.containsKey(keyColumn.getKey()) && filterObj.get(keyColumn.getKey()).equals(keyLevelDataObj.getKeyColumns().get(keyColumn.getKey()))) {
						selectedKeyDups.add(filterObj);
					}
				});
				filteredList.clear();
				filteredList.addAll(selectedKeyDups);
			});
			if(filteredList.size() == keyLevelDataObj.getObjectsIds().size()) {
				throw new BadRequestException("Cannot Perform Delete");
			}else {
				keyLevelDataObj.setCanDelete(true);
			}
		});
		requestData.getKeyDuplicateData().forEach(keyLevelDataObj ->{
			if(keyLevelDataObj.getCanDelete() == true) {
				keyLevelDataObj.getObjectsIds().forEach(element->{
					Optional<KeyLevelDuplicates> data = keyLevel.findById(element);
					data.get().setDeleteFlag("Y");
					data.get().setDeletedBy(deletedBy);
					keyLevel.save(data.get());
					});
			}
		});
			 
		return Message.BOOLEAN_RESPONSE + "Y";
	}
	
	private KeyLevelDeleteRequest getDeleteDataFromRequest(KeyLevelRemoveRequest requestObj) {
		KeyLevelDeleteRequest requestData = new KeyLevelDeleteRequest();
		requestData.setExamID(requestObj.getExamID());
		requestData.setExcelSheetName(requestObj.getExcelSheetName());
		List<KeyLevelDeleteData> keyDuplicateData = new ArrayList<KeyLevelDeleteData>();
		Map<String,KeyLevelDeleteData> uniqueKeyMap = new HashMap<String,KeyLevelDeleteData>();
		List<String> keyColumns = Arrays.asList(requestObj.getKeyColumns().split("\\s*,\\s*"));
		requestObj.getKeyDuplicateData().forEach(keyLevelObj ->{
			Map<String,String> generateKeyColumnMap = new HashMap<String,String>();
			keyLevelObj.put("uniqueKey","");
			keyColumns.forEach(keyColumnObj ->{
				keyLevelObj.put("uniqueKey",(keyLevelObj.get("uniqueKey")+keyLevelObj.get(keyColumnObj)));
				generateKeyColumnMap.put(keyColumnObj, keyLevelObj.get(keyColumnObj));
			});	
			if(uniqueKeyMap.containsKey(keyLevelObj.get("uniqueKey"))) {
				KeyLevelDeleteData data = uniqueKeyMap.get(keyLevelObj.get("uniqueKey"));
				List<String> objectIdList = data.getObjectsIds();
				objectIdList.add(keyLevelObj.get("id"));
				uniqueKeyMap.put(keyLevelObj.get("uniqueKey"), data);
				
			}else {
				List<String> objectIdList = new ArrayList<String>();
				objectIdList.add(keyLevelObj.get("id"));
				KeyLevelDeleteData data = KeyLevelDeleteData.builder().keyColumns(generateKeyColumnMap).objectsIds(objectIdList).canDelete(false).build();
				uniqueKeyMap.put(keyLevelObj.get("uniqueKey"), data);
			}
		});
		uniqueKeyMap.keySet().forEach(unique->{
			keyDuplicateData.add(uniqueKeyMap.get(unique));
		});
		requestData.setKeyDuplicateData(keyDuplicateData);
		return requestData;
	}
	
//	API FOR DOWNLOAD KEY LEVEL DUPLICATES DATA
	@Override
	public List<Map<String, String>> downloadKeyLevelData(int examId,String excelName) throws RuntimeException {
		return getKeyLevelDuplicatesResponse(keyLevel.findAllByExamIDAndDeleteFlagAndRecordValueAndExcelSheetName(examId,"N","KEY-DUP",excelName));
	
	}

//API FOR GET EXAM SECTION FIELD DATA
	@Override
	public List<ExamLinecardSections> getExamLinecardSection(int examId) throws RuntimeException {
		Exam examData = examRepo.findAllByExamID(examId);
		return examData.getLinecardSections();
	}

//	API FOR GET AUDIT COLL  DATA
	@Override
	public List<AuditColl> getAuditCollData(int examId) throws RuntimeException {
		return auditCollRepo.findAllByExamID(examId);
	}

//	API FOR UPDATE AUDIT COLL  DATA
	@Override
	public AuditColl updateAuditCollData(long examId,String status,String excelName) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		if(status.equalsIgnoreCase("ABANDON")) {
			AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
			List<AuditHistory> audit = auditData.getAuditHistory();
			String currentStatus = auditData.getCurrentStatus();
			LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
			if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
			AuditHistory auditHistory = new AuditHistory();
			auditHistory.setStatus(currentStatus);
			auditHistory.setUpdatedBy(auditData.getUpdatedBy());
			auditHistory.setUpdatedTime(currentDate);
			audit.add(auditHistory);
			}
			LocalDateTime dateTime = java.time.LocalDateTime.now();
			auditData.setCurrentStatus(status);
			auditData.setUpdatedBy("UI");
			auditData.setCurrentUpdatedDate(dateTime);
			auditCollRepo.save(auditData);
		}
		else {
			AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
			List<AuditHistory> audit = auditData.getAuditHistory();
			String currentStatus = auditData.getCurrentStatus();
			LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
			if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
				AuditHistory auditHistory = new AuditHistory();
				auditHistory.setStatus(currentStatus);
				auditHistory.setUpdatedBy(auditData.getUpdatedBy());
				auditHistory.setUpdatedTime(currentDate);
				audit.add(auditHistory);
			}
			LocalDateTime dateTime = java.time.LocalDateTime.now();
			auditData.setCurrentStatus(status);
			auditData.setUpdatedBy("UI");
			auditData.setCurrentUpdatedDate(dateTime);
			auditCollRepo.save(auditData);
			checkAuditStatus(auditData,examId,excelName);
		}
		AuditColl auditDataFinal = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
		return auditDataFinal;
	}
	public void checkAuditStatus(AuditColl auditData,long examId,String excelName) {
		boolean mappingReviewedExist  = auditData.getAuditHistory().stream().anyMatch(auditHistoryObj -> auditHistoryObj.getStatus().equalsIgnoreCase("MAPPING REVIEWED"));
		boolean duplicateReviewedExist = auditData.getAuditHistory().stream().anyMatch(auditHistoryObj -> auditHistoryObj.getStatus().equalsIgnoreCase("DUPLICATE REVIEWED"));
		if(auditData.getCurrentStatus().equalsIgnoreCase("DUPLICATE REVIEWED"))
			duplicateReviewedExist = true;
		if(auditData.getCurrentStatus().equalsIgnoreCase("MAPPING REVIEWED"))
			mappingReviewedExist = true;
		if(mappingReviewedExist && duplicateReviewedExist) {
			updateAuditCollLoadToTarget(examId,excelName);
		}
	}
	
	public void updateAuditCollLoadToTarget(long examId,String excelName) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);;
		List<AuditHistory> audit = auditData.getAuditHistory();
		String currentStatus = auditData.getCurrentStatus();
		LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
		if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setStatus(currentStatus);
		auditHistory.setUpdatedBy("UI");
		auditHistory.setUpdatedTime(currentDate);
		audit.add(auditHistory);
		}
		LocalDateTime dateTime = java.time.LocalDateTime.now();
		auditData.setCurrentStatus("LOAD TO TARGET");
		auditData.setCurrentUpdatedDate(dateTime);
		auditCollRepo.save(auditData);
	}
	
	public String getCurrentTime() {
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		   LocalDateTime now = LocalDateTime.now();
		   return dtf.format(now);
	}

//	API FOR GET ROW LEVEL DUPLICATES  DATA
	@Override
	public RowLevelDuplicatesPageableDTO getRowLevelDuplicatesData(long examId,Pagination pagination,String excelName) throws RuntimeException {
		List<ExamResponseDTO> examResponse = new ArrayList<ExamResponseDTO>();
		Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
				Sort.by(pagination.getSort()).descending());
		Page<RowLevelDuplicates> data = rowLevel.findByExamIDAndRecordValueAndExcelSheetName(examId,pageable,"ROW-DUP",excelName);
		List<Map<String,String>> examResponseDTO = getRowLevelDuplicatesResponse(data.getContent());
		return RowLevelDuplicatesPageableDTO.builder().result(examResponseDTO).pageable(data.getPageable())
//				.totalPages(data.getTotalPages()).totalElements((long)examResponseDTO.size()).build();
				.totalPages(data.getTotalPages()).totalElements((long)data.getTotalElements()).build();
	}
	
	public List<Map<String, String>> getRowLevelDuplicatesResponse(List<RowLevelDuplicates> data) {
		List<Map<String, String>> examResponse = new ArrayList<Map<String, String>>();
		data.forEach(element->{
			Map<String, String> ExamResponseMap = new HashMap<>();
			ExamResponseMap.put("id", element.getId());
			ExamResponseMap.put("exam_id", Long.toString(element.getExamID()));
			ExamResponseMap.put("row_hash", element.getRowHash());
			ExamResponseMap.put("rec_id", element.getRecordValue());
			ExamResponseMap.put("excel_sheet_name", element.getExcelSheetName());
			List<KeyLevelColumns> columnValue = element.getData();
			columnValue.forEach(colValue-> {
				ExamResponseMap.put(colValue.getFname(), colValue.getFvalue());
//				ExamResponseMap.put("keyHash", element.getKeyHash());
			});
			examResponse.add(ExamResponseMap);
		});
		return examResponse;
	}
	


//	API FOR DOWNLOAD ROW LEVEL DUPLICATES DATA
	@Override
	public List<Map<String,String>> downloadRowLevelDuplicates(int examId,String excelName) throws RuntimeException {
		return getRowLevelDuplicatesResponse(rowLevel.findAllByExamIDAndRecordValueAndExcelSheetName(examId,"ROW-DUP",excelName));	
	}

//	API FOR GET DATA SATGE1
	@Override
	public List<Map<String, String>> getDataStage1(int examId,String excelName) throws RuntimeException {
		Pagination pagination = RequestFormatter.getPagination(0,10,"examId");
		PageRequest pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
				Sort.by(pagination.getSort()).descending());
		Page<DataStage1> data = dataStage1Repo.findAllByExamIDAndDeleteFlagAndExcelSheetName(examId,"N",excelName,pageable);
		return getDataStageResponse(data);
	}
	
	public List<Map<String, String>> getDataStageResponse(Page<DataStage1> data) {
		List<Map<String, String>> examResponse = new ArrayList<Map<String, String>>();
		data.forEach(element->{
			Map<String, String> ExamResponseMap = new HashMap<>();
			ExamResponseMap.put("id", element.getId());
			ExamResponseMap.put("exam_id", Integer.toString(element.getExamID()));
			ExamResponseMap.put("row_hash", element.getRowHash());
			ExamResponseMap.put("key_hash", element.getKeyHash());
			ExamResponseMap.put("excel_sheet_name", element.getExcelSheetName());
			List<Stage1Column> columnValue = element.getData();
			columnValue.forEach(colValue-> {
				ExamResponseMap.put(colValue.getFname(), colValue.getFvalue());
			});
			examResponse.add(ExamResponseMap);
		});
		return examResponse;
	}

//	API FOR GET DATA DICTIONARY
	@Override
	public List<DataDictionary> getDataDictionary(long examId,String excelName) throws RuntimeException {
//		List<DataDictionaryResponseDTO> examResponse = new ArrayList<DataDictionaryResponseDTO>();
		List<DataDictionary> data = dataDictionaryRepo.findByExamIDAndExcelSheetName(examId,excelName); 
//		data.forEach(e->{
//			String targetCol = e.getTargetColumn();
//			if(!(targetCol == null)) {
//				Optional<ConsumerFields> field = consumerFieldsRepo.findAllByFieldName(targetCol);
//				if(field.isPresent()) {
//					ConsumerFields fieldData = field.get();
//					DataDictionaryResponseDTO setData = setResponseInDataDictionary(e,fieldData);
//					examResponse.add(setData);
//				}
//				else {
//					DataDictionaryResponseDTO setData = setResponseInDataDictionary(e,null);
//					examResponse.add(setData);
//				}
//			}
//			else {
//				DataDictionaryResponseDTO setData = setResponseInDataDictionary(e,null);
//				examResponse.add(setData);
//			}
//			
//		});
		return data;
	}   
	
//	public DataDictionaryResponseDTO setResponseInDataDictionary(DataDictionary dataDictionary,ConsumerFields id) {
//		DataDictionaryResponseDTO examResponse = DataDictionaryResponseDTO.builder().id(dataDictionary.getId() == null ? null :dataDictionary.getId() )
//				.examID(dataDictionary.getExamID()).excelSheetName(dataDictionary.getExcelSheetName())
//				.sourceColumn(dataDictionary.getSourceColumn()).targetColumn(dataDictionary.getTargetColumn())
//				.fieldID(id == null ? 0 : id.getFieldID()).build();
//		return examResponse;
//	}

	/*@Override
	public String updateDataDictionary(List<DataDictionary> data) throws RuntimeException {
		data.forEach(element->{
			Optional<DataDictionary> dataDictionary = dataDictionaryRepo.findById(element.getId());
			dataDictionary.get().setTargetColumn(element.getTargetColumn());
			dataDictionaryRepo.save(dataDictionary.get());
		});

		return Message.BOOLEAN_RESPONSE + "Updated";
	}*/

	@Override
	public List<ConsumerFields> getConsumerField(int examId) throws RuntimeException {
		List<ConsumerFields> consumerResponse = new ArrayList<ConsumerFields>();
		Optional<Exam> consumerExamData = examRepo.findByExamID(examId);
		List<ExamFields> consumerFieldData = consumerExamData.get().getFields();
		if(!CollectionUtils.isEmpty(consumerFieldData)){
			consumerFieldData.forEach(element->{
				long id = element.getFieldID();
				long linecardId = element.getLinecardIDSecOverride();
				String caption = element.getCaptionOverride();
				Optional<ConsumerFields> data = consumerFieldsRepo.findByFieldIDAndCollName(id,"tclDataExamSpecific");
				if(data.isPresent()) {
					consumerResponse.add(setResponse(data.get(),findLinecardSectionDataById(linecardId),caption,linecardId));
				}
			});
		}
		return consumerResponse;
	}
	
	public ConsumerLinecardSection findLinecardSectionDataById(long linecardId) {
		Optional<ConsumerLinecardSection> sectionData = consumerLinecardSectionRepo.findBySectionID(linecardId);
		if(sectionData.isPresent()) {
			return sectionData.get();
		}else {
			return null;
		}
		
	}

	
	public ConsumerFields setResponse(ConsumerFields data,ConsumerLinecardSection consumerSection,String caption,long sectionId) {
		ConsumerFields examResponse = ConsumerFields.builder().id(data.getId()).fieldID(data.getFieldID())
				.fieldName(data.getFieldName()).collName(data.getCollName()).caption(caption == null? "":caption).section(consumerSection == null? "":consumerSection.getSection())
				.sectionID(sectionId).controlName(data.getControlName()).secDisc(data.getSecDisc()).toolTip(data.getToolTip())
				.reportSectionID(data.getReportSectionID()).required(data.isRequired()).dateAdded(data.getDateAdded()).sortOrder(data.getSortOrder())
				.allowCaptionOverride(data.isAllowCaptionOverride()).dataType(data.getDataType()).updatedDate(data.getUpdatedDate()).updatedBy(data.getUpdatedBy())
				.alias(data.getAlias()).build();
				return examResponse;
	}
	
//API FOR SAVE ON FIELD MAPPING ADD NEW SECTION  IN CONSUMER FIELD COLLECTION
	@Override
	public String addConsumerFieldData(ConsumerFields consumerField,int sectionId,int examId) throws RuntimeException {
		
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "fieldID"));
		query.limit(1);
		ConsumerFields maxObject = mongoTemplate.findOne(query, ConsumerFields.class);
		Long fieldId = maxObject.getFieldID();
		fieldId++;
		consumerField.setCollName("tclDataExamSpecific");
		consumerField.setFieldID(fieldId);
		consumerField.setSectionID(sectionId);
		boolean status = checkDuplicate(consumerField.getFieldName());
		if(status) {
			consumerFieldsRepo.save(consumerField);
		}
		else {
			return Message.BOOLEAN_RESPONSE + "Field Name is already exist";
		}
		addFieldInConsumerExam(examId,fieldId);
		return Message.BOOLEAN_RESPONSE + "Data Insert Successfully";
	}

	
	//API FOR SAVE ON FIELD MAPPING ADD NEW SECTION  IN CONSUMER EXAM COLLECTION
	public void addFieldInConsumerExam(int examId, long fieldId) {
		
		Optional<ConsumerFields> fieldData = consumerFieldsRepo.findByFieldID(fieldId);
		String caption = fieldData.get().getCaption();
		long section = fieldData.get().getSectionID();
		if(fieldData.isPresent()) {
			Optional<Exam> examCollection = examRepo.findByExamID(examId);
			List<ExamFields> list = examCollection.get().getFields();
			if(CollectionUtils.isEmpty(list)){
				list = new ArrayList<>();
			}
			ExamFields examField = new ExamFields();
			examField.setFieldID(fieldId);
			examField.setCaptionOverride(caption);
			examField.setLinecardIDSecOverride(section);
			list.add(examField);
			examCollection.get().setFields(list);
			examRepo.save(examCollection.get());
		}
	}
	
	private Long generateUniqueFieldId() {
		boolean invalidFieldId = true;
		long fieldId = 0;
		while(invalidFieldId) {
			fieldId = FieldIdGenerator.getFieldId();
			Optional<ConsumerFields> consumerData = consumerFieldsRepo.findByFieldID(fieldId);
		    if(!consumerData.isPresent())
		    	invalidFieldId = false;
		}
		return fieldId; 
	}
	
	public boolean checkDuplicate(String dbFieldName) {
		ConsumerFields data = consumerFieldsRepo.findByFieldName(dbFieldName);
		if(data == null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public List<ConsumerLinecardSection> getExamSectionData(int examId) throws RuntimeException {
		List<ConsumerLinecardSection> consumerResponse = new ArrayList<ConsumerLinecardSection>();
		Optional<Exam> consumerExamData = examRepo.findByExamID(examId);
		List<ExamLinecardSections> consumerLinecardSection = consumerExamData.get().getLinecardSections();
		if(!CollectionUtils.isEmpty(consumerLinecardSection)){
			consumerLinecardSection.forEach(element->{
				long id = element.getSectionID();
				Optional<ConsumerLinecardSection> data = consumerLinecardSectionRepo.findBySectionID(id);
				consumerResponse.add(data.get());
			});
		}

		return consumerResponse;
	}
    
	@Transactional
	@Override
	public String updateOnCompleteMapping(CompleteMapping object) throws RuntimeException {
		long examId = object.getExamID();
		String excelName = object.getExcelSheetName();
		List<DataDictionaryDTO> data = object.getDataDictionaryList();
		data.forEach(element->{
			String id = element.getId();
			long fieldId = element.getFieldID();
			if(fieldId > 0) {
				String targetColumn = element.getTargetColumn();
				String sourceColumn = element.getSourceColumn();
				updateDataDictionary(id,targetColumn,element.getFieldID());
				saveFieldDataInConsumerExam(examId,fieldId);
				addAliasInConsumerField(fieldId,sourceColumn);
			}
		});
		updateAuditColl(examId,excelName);
		return Message.BOOLEAN_RESPONSE + "Data update successfully";
	}
	
	
	public void updateDataDictionary(String id, String targetColumn,long fieldId)  {
			Optional<DataDictionary> dataDictionary = dataDictionaryRepo.findById(id);
			dataDictionary.get().setTargetColumn(targetColumn);
			dataDictionary.get().setFieldID((targetColumn == null && fieldId != 0) ? null : fieldId);
			dataDictionaryRepo.save(dataDictionary.get());
		}
	
	public void updateAuditColl(long examId,String excelName) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);;
		List<AuditHistory> audit = auditData.getAuditHistory();
		String currentStatus = auditData.getCurrentStatus();
		LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
		if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setStatus(currentStatus);
		auditHistory.setUpdatedBy("UI");
		auditHistory.setUpdatedTime(currentDate);
		audit.add(auditHistory);
		}
		LocalDateTime dateTime = java.time.LocalDateTime.now();
		auditData.setCurrentStatus("MAPPING REVIEWED");
		auditData.setCurrentUpdatedDate(dateTime);
		auditCollRepo.save(auditData);
		checkAuditStatus(auditData,examId,excelName);
	}
	
	public void saveFieldDataInConsumerExam(long examId, long fieldId) {
		
		Optional<Exam> examData = examRepo.findByExamID(examId);
		List<ExamFields> examFieldsList = examData.get().getFields();
		ExamFields exmaField = examFieldsList.stream()
				  .filter(examFieldObj -> examFieldObj.getFieldID() == fieldId)
				  .findAny()
				  .orElse(null);	
		if(exmaField == null) {
			
			Optional<ConsumerFields> fieldData = consumerFieldsRepo.findByFieldID(fieldId);
			if(fieldData.isPresent()) {
				boolean required = fieldData.get().isRequired();
				String caption = fieldData.get().getCaption();	
				long section = fieldData.get().getSectionID();
				long report = fieldData.get().getReportSectionID();
				ExamFields examField = new ExamFields();
				examField.setFieldID(fieldId);
				examField.setRequired(required);
				examField.setCaptionOverride(caption);
				examField.setLinecardIDSecOverride(section);
				examField.setReportSecIDOverride(report);
				examFieldsList.add(examField);
				examRepo.save(examData.get());
			}
				
			}
	}
	
	public void addAliasInConsumerField(long fieldId, String sourceColumn) {
		Optional<ConsumerFields> data = consumerFieldsRepo.findByFieldID(fieldId);
		if(data.isPresent()) {
			List<String> alias = new ArrayList<>();
			if(!StringUtils.isEmpty(data.get().getAlias())) {
				alias = Stream.of(data.get().getAlias().split(","))
                        .collect(Collectors.toList());
			}
			if(CollectionUtils.isEmpty(alias)){
				data.get().setAlias(sourceColumn);
				consumerFieldsRepo.save(data.get());
			}
			else {
				String consumerFieldData = alias.stream().filter(aliasObj -> aliasObj.equalsIgnoreCase(sourceColumn))
					 .findAny()
					 .orElse(null);
					if(consumerFieldData == null) {
						alias.add(sourceColumn);
						data.get().setAlias(String.join(",", alias));
						consumerFieldsRepo.save(data.get());
					}
			}
		}
	}

	@Override
	public String EditOnAddNewScreen(long examId, long fieldId,String caption, int section) throws RuntimeException {
		Optional<Exam> examData = examRepo.findByExamID(examId);
		List<ExamFields> examFieldData = examData.get().getFields();
		examFieldData.forEach(element->{
			long id = element.getFieldID();
			if(id == fieldId) {
				element.setCaptionOverride(caption);
				element.setLinecardIDSecOverride((long)section);
				examRepo.save(examData.get());
			}
		});
		return Message.BOOLEAN_RESPONSE + "Data Updated Successfully";
	}

	@Override
	public String deleteFieldMapping(long examId, long fieldId) throws RuntimeException {
		Optional<Exam> examData = examRepo.findByExamID(examId);
		List<ExamFields> examFieldData = examData.get().getFields();
		for(int i=0; i<examFieldData.size(); i++) {
			if(examFieldData.get(i).getFieldID() == fieldId) {
				examFieldData.remove(i);
				examRepo.save(examData.get());
			}
		};
		return Message.BOOLEAN_RESPONSE + "Data Updated Successfully";
	}

	@Override
	@Transactional
	public String updateReviewAndLoad(long examId,String excelName) throws RuntimeException {
		updateAuditLoadInProgress(examId,excelName);
		List<DataStage3> data = dataStage3Repo.findByExamIDAndDeleteFlagAndExcelSheetName(examId,"N",excelName);
		List<DataDictionary> dataDictionary = dataDictionaryRepo.findByExamIDAndExcelSheetName(examId, excelName);
		//List<List<DataStage3>> dataChunks = getListChunks(100,data);
		List<ConsumerLinecard> lineCardList = new ArrayList<ConsumerLinecard>();
		AtomicLong count = new AtomicLong();
		data.forEach(element->{
			count.getAndIncrement();
		    UUID uid = UUID.fromString("006C819A-8EE6-44F3-B8A0-9BD795143339"); 
			UUID hexadecimal = uid.randomUUID();
			String uuid = hexadecimal.toString();
			String excelSheetName = element.getExcelSheetName();
			ConsumerLinecard consumerLinecard = new ConsumerLinecard();
			consumerLinecard.setExamID(element.getExamID());
			consumerLinecard.setLinecardID(uuid);
			consumerLinecard.setSourceDataFile(excelSheetName);
			lineCardList.add(consumerLinecard);
			//consumerLinecardRepo.save(consumerLinecard);
			setDataInConsumerExamAppData(element,uuid,element.getExamID(),excelName,dataDictionary);
		});
		if(!CollectionUtils.isEmpty(lineCardList)) {
			saveBulkConsumerLineCards(1000,lineCardList);
		}
		updateAuditCollOnTargetToLoad(examId, count.get(),excelName);
		return Message.BOOLEAN_RESPONSE + "Data insert successfully";
	}
	
	private void saveBulkConsumerLineCards(int chunkSize,List<ConsumerLinecard> lineCards) {
		AtomicInteger counter = new AtomicInteger();
		 Collection<List<ConsumerLinecard>> chunks = lineCards.stream()
		    .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
		    .values();
		 List<List<ConsumerLinecard>> chunksList = chunks.stream().collect(Collectors.toList());
		 chunksList.forEach(lineCardChunk ->{
			 consumerLinecardRepo.saveAll(lineCardChunk);
		 });
		
	}

	public void setDataInConsumerExamAppData(DataStage3 data,String linecardId,long examId,String excelName,List<DataDictionary> dataDictionary) {
		Map<String, Object> ExamResponseMap = new HashMap<>();
		ExamResponseMap.put("linecardID", linecardId);
		String excelSheetName = data.getExcelSheetName();
		List<Stage1Column> columnData = data.getData();
		columnData.forEach(element->{
			String fName = element.getFname();
			DataDictionary dictionary = dataDictionary.stream().filter(dataDictionaryObj -> dataDictionaryObj.getSourceColumn().equalsIgnoreCase(fName))
					 .findAny()
					 .orElse(null);
			if(dictionary != null ) {
				String targetColumn = dictionary.getTargetColumn();
				if(!StringUtils.isEmpty(targetColumn)) {
					ExamResponseMap.put(targetColumn, element.getFvalue());
				}
			}
			else {
				ExamResponseMap.put(element.getFname(), element.getFvalue());
			}
		});
		ExamResponseMap.put("examID", examId);
		ExamResponseMap.put("excelSheetName", excelName);
		mongoTemplate.save(ExamResponseMap, "ConsumerExamAppData");
	}
	
	public void updateAuditCollOnTargetToLoad(long examId,long count,String excelName) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
		List<AuditHistory> audit = auditData.getAuditHistory();
		String currentStatus = auditData.getCurrentStatus();
		LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
		if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setStatus(currentStatus);
		auditHistory.setUpdatedBy("UI");
		auditHistory.setUpdatedTime(currentDate);
		audit.add(auditHistory);
		}
		LocalDateTime dateTime = java.time.LocalDateTime.now();
		auditData.setCurrentStatus("LOADED TO TARGET");
		auditData.setCurrentUpdatedDate(dateTime);
		auditData.setNumberOfRecordLoaded(count);
		auditCollRepo.save(auditData);
	}
	
	public void updateAuditLoadInProgress(long examId,String excelName) throws RuntimeException {
		Sort sort = new Sort(Sort.Direction.DESC , "updatedDate");
		AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
		List<AuditHistory> audit = auditData.getAuditHistory();
		String currentStatus = auditData.getCurrentStatus();
		LocalDateTime currentDate = auditData.getCurrentUpdatedDate();
		if(!AppUtil.checkAlreadyStatusExist(audit, currentStatus)) {
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setStatus(currentStatus);
		auditHistory.setUpdatedBy("UI");
		auditHistory.setUpdatedTime(currentDate);
		audit.add(auditHistory);
		}
		LocalDateTime dateTime = java.time.LocalDateTime.now();
		auditData.setCurrentStatus("LOAD IN PROGRESS");
		auditData.setCurrentUpdatedDate(dateTime);
		auditCollRepo.save(auditData);
	
	}
	

	@Override
	public boolean checkAlreadyDuplicateReviewed(long examId, String excelName) throws RuntimeException {
		AuditColl auditData = auditCollRepo.findByExamIDAndExcelSheetName(examId,excelName);
		if(!ObjectUtils.isEmpty(auditData)) {
			if(!CollectionUtils.isEmpty(auditData.getAuditHistory())) {
				boolean duplicateReviewExist = auditData.getAuditHistory().stream().anyMatch(auditHistoryObj -> auditHistoryObj.getStatus().equalsIgnoreCase(Constants.DUPLICATE_REVIEWED));
				return duplicateReviewExist;
			}
		}
		return false;
	}
	
	
	
	//API FOR FIND LOOKUPTYPE DATA
		@Override
		public List<LookupTypeResponseDTO> getSystmLookup(int lookupTypeId) throws RuntimeException {
			Optional<LookupType> data = lookup.findByLookupTypeID(lookupTypeId);
			if(data.isPresent()) {
				List<LookupTypeResponseDTO> lookupValue = data.get().getLookupValue();
				return lookupValue.stream().filter(Predicate->Predicate.isActive()).collect(Collectors.toList());
			}
			else {
				return null;
			}
		}

		@Override
		public String updateExamType(int examId, int lookupTypeId, String value) throws RuntimeException {
			Optional<Exam> examData = examRepo.findByExamID(examId);
			if(examData.isPresent()) {
				examData.get().setExamType(value);
				examRepo.save(examData.get());
				return Message.BOOLEAN_RESPONSE + "EXAM TYPE UPDATED SUCCESSFULLY";
			}
			else {
				return Message.BOOLEAN_RESPONSE + NO_DATA_FOUND;
			}
		}

		@Override
		public List<ProjectMasterDataResponseDTO> getProjectMasterData() throws RuntimeException {
			List<ProjectMasterDataResponseDTO> projectResponse = new ArrayList<ProjectMasterDataResponseDTO>();
			List<Integer> dates = new ArrayList<>();
			
			long OnyDayMileSecond = 24 * 60 * 60 * 1000; 
			Date currentDate = new Date();
			int year = currentDate.getYear();
			year = year + 1900;
			dates.add(year);
			int nextDateYear = year+1;
			dates.add(nextDateYear);
			int previousDateYear = year-1;
			dates.add(previousDateYear);
						
			List<ProjectMaster> data = proMaster.findByExamDataTypeIgnoreCaseAndProjectStatusNotIgnoreCaseAndProjectLUNameIsNotNullAndYearExaminedIn("consumer","cancelled",dates);
			data.forEach(element->{
				Long eicId = element.getEicID();
				
				String examiner = getExaminerData(eicId);
				String guestExaminer = getGuestExaminerData(eicId);
				String examinerName = null;
				if(!(examiner == null) && !(guestExaminer == null)){
					examinerName = guestExaminer;
				}
				else if(!(examiner == null)) {
					examinerName = examiner;
				}
				else if(!(guestExaminer == null)) {
					examinerName = guestExaminer;
				}
						
				long projectId = element.getProjectID();
				String luName = element.getProjectLUName();
				ProjectMasterDataResponseDTO newExam = new ProjectMasterDataResponseDTO();
				newExam.setProjectID(projectId);
				newExam.setProjectLUName(luName);
				newExam.setEicID(eicId);
				newExam.setExaminerName(examinerName);
				projectResponse.add(newExam);
			});

			List<ProjectMasterDataResponseDTO> response = projectResponse.stream().limit(1000).collect(Collectors.toList());
			Collections.reverse(response);
			return response;
		}
		
		private String getExaminerData(long eicId) {
			Optional<ExaminerInformation> data = examInfo.findByExaminerID(eicId);
			if(data.isPresent()) {
				String firstName = data.get().getFirstName();
				String lastName = data.get().getLastName();
				String result = firstName + " " + lastName;
				return result;
			}
			else {
				return null;
			}
		}
		
		private String getGuestExaminerData(long eicId){
			Date date = new Date();
			Optional<ConsumerGuestExaminer> data = guestExaminerRepo.findByExaminerIDAndEndDateGreaterThan(eicId,date);
			if(data.isPresent()) {
				String firstName = data.get().getFirstName();
				String lastName = data.get().getLastName();
				String result = firstName + " " + lastName;
				return result;
			}
			else {
				return null;
			}
			
		}


		@Override
		public String getEicName(long projectId) throws RuntimeException {
		Optional<ProjectMaster> data = proMaster.findByProjectID(projectId);
		if(data.isPresent()) {
			Long eicId = data.get().getEicID();
			if(eicId != null){
				Optional<ExaminerInformation> examInfoData = examInfo.findByExaminerID(eicId);
				if(examInfoData.isPresent()) {
					String firstName = examInfoData.get().getFirstName();
					String lastName = examInfoData.get().getLastName();
					String result = firstName + " " + lastName;
					return result;
				}
				else {
					return Message.BOOLEAN_RESPONSE + NO_DATA_FOUND;
				}
			}
			return "";
		}
		else {
			return Message.BOOLEAN_RESPONSE + NO_DATA_FOUND;
		}
			
		}

		@Override
		public String addNewConsumerExam(ExamParameterResponse exam) throws RuntimeException {
			Query query = new Query();
			query.with(new Sort(Sort.Direction.DESC, "ExamID"));
			query.limit(1);
			Exam maxObject = mongoTemplate.findOne(query, Exam.class);
			 Long maxExamId = maxObject.getExamID();
			 maxExamId++;
			 
			 List<ExamFields> field = setExamFields(exam.getExamType());
			 List<ExamLinecardSections> linecardSections = setLineCardSectionsForExam(exam.getExamTypeID());
			 String name = exam.getExamName();
			 String type = exam.getExamType();
			 Long prjectId = exam.getProjectID();
			 Exam newExam = new Exam();
			 newExam.setExamID(maxExamId);
			 newExam.setExamName(name);
			 newExam.setExamType(type);
			 newExam.setProjectID(prjectId);
			 newExam.setArchived(false);
			 newExam.setActive(true);
			 newExam.setDeleted(false);
			 newExam.setFields(field);
			 newExam.setLinecardSections(linecardSections);
			 examRepo.save(newExam);
			 setSuppressAndFieldStatus(maxExamId,exam.getExamTypeID());
			 
//			 createNewExamInAditCollection(number,excelName,keyColumns);
			 return Message.BOOLEAN_RESPONSE + "Data Created Successfully";
		}
		
		private List<ExamLinecardSections> setLineCardSectionsForExam(long examTypeId){
			List<ExamLinecardSections> linecardSections = new ArrayList<ExamLinecardSections>();
			List<Long> examTypeIds = Arrays.asList(examTypeId);
			Optional<List<ConsumerGenericTemplate>> genericTemplates = consumerGenericTemplateRepo.findByExamTypeIDIn(examTypeIds);
			if(genericTemplates.isPresent()) {
				genericTemplates.get().forEach(genericTemplate ->{
					ExamLinecardSections examLineCardSection = new ExamLinecardSections();
					examLineCardSection.setSectionID(genericTemplate.getLinecardSectionID());
					examLineCardSection.setPgBrkOverride(genericTemplate.isPageBreak());
					examLineCardSection.setReportOrderOverride(genericTemplate.getReportOrder());
					examLineCardSection.setLinecardOverride(genericTemplate.getLinecardOrder());
					examLineCardSection.setSuppress(false);
					linecardSections.add(examLineCardSection);
				});
			}
			return linecardSections;
		}
		
		public List<ExamFields> setExamFields(String examType){
			List<ConsumerFields> consumerFieldsData = getConsumerFieldForNewExam(examType);
			List<ExamFields> fields = new ArrayList<>();
			consumerFieldsData.forEach(element->{
				long fieldId = element.getFieldID();
				boolean required = element.isRequired();
				String captionOverride = element.getCaption();
				long linecardSecIDOverride = element.getSectionID();
				long report = element.getReportSectionID();
				ExamFields newField = new ExamFields();
				newField.setFieldID(fieldId);
				newField.setRequired(required);
				newField.setCaptionOverride(captionOverride);
				newField.setLinecardIDSecOverride(linecardSecIDOverride);
				newField.setReportSecIDOverride(report);
				newField.setMapped(0L);
				newField.setFieldStatus(null);
				newField.setSupress(false);
				fields.add(newField);			
			});
			return fields;
		}
		
		public List<ConsumerFields> getConsumerFieldForNewExam(String examType)  {
			Optional<LookupType> consumerExamLookupData = lookup.findByLookupType(Constants.CONSUMER_EXAM_TYPE);
			if(consumerExamLookupData.isPresent()) {
			List<LookupTypeResponseDTO> originationsLookup = consumerExamLookupData.get().getLookupValue().stream().filter(lookupObj -> lookupObj.getValue1().equals(examType))
					  .collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(originationsLookup)) {
				 List<Long> examTypeIds = originationsLookup.stream().map(examTypeObj -> examTypeObj.getLookupID()).collect(Collectors.toList());
				 Optional<List<ConsumerGenericTemplate>> consumerGenericTemplateList = consumerGenericTemplateRepo.findByExamTypeIDIn(examTypeIds);
				 if(consumerGenericTemplateList.isPresent()) {
					 List<Long> genericLineCardSecIds = consumerGenericTemplateList.get().stream().map(genericTemplateObj -> genericTemplateObj.getLinecardSectionID()).collect(Collectors.toList());
					 Optional<List<ConsumerLinecardSection>> consumerLineCardSections = consumerLinecardSectionRepo.findBySectionIDIn(genericLineCardSecIds);
					 if(consumerLineCardSections.isPresent()) {
						 List<Long> lineCardSecIds = consumerLineCardSections.get().stream().map(consumerLineCardSection -> consumerLineCardSection.getSectionID()).collect(Collectors.toList());
						 List<String> collNames = Arrays.asList("tclDataGeneric");
						 return consumerFieldsRepo.findBySectionIDInAndCollNameIn(lineCardSecIds, collNames).get();
					 }
				 }
			}
			}
			return null;
		}
		
		public void setSuppressAndFieldStatus(long examId,long examTypeId) {
			Optional<Exam> examData = examRepo.findByExamID(examId);
			if(examData.isPresent()) {
				List<ExamFields> fields = examData.get().getFields();
				fields.forEach(element->{
					String caption = element.getCaptionOverride();
					if((examTypeId == 2 || examTypeId == 3 || examTypeId == 5) && (caption.equalsIgnoreCase("Account Number"))) {
						element.setSupress(true);	
					}
					else if(caption.equalsIgnoreCase("Application Number")) {
						element.setSupress(true);
					}
					else {
						element.setSupress(false);
					}
					
					if(caption.contains("LOB")) {
						element.setFieldStatus("read-only");
					}
				});	
			}
			examRepo.save(examData.get());
		}
		
	
//		public void createNewExamInAditCollection(long examId,String excelName,List<String> keyColumns) {
//			LocalDateTime dateTime = java.time.LocalDateTime.now();
//			AuditColl newAuditExam = new AuditColl();
//			List<AuditHistory> auditH = new ArrayList<AuditHistory>();
//			newAuditExam.setExamID(examId);
//			newAuditExam.setCurrentStatus("IMPORT COMPLETED");
//			newAuditExam.setUpdatedBy("UI");
//			newAuditExam.setCurrentUpdatedDate(dateTime);
//			newAuditExam.setExcelSequenceNumber(1);
//			newAuditExam.setExcelSheetName(excelName);
//			newAuditExam.setKeyColumns(keyColumns);
//			newAuditExam.setAuditHistory(auditH);
//			auditCollRepo.save(newAuditExam);
//		}

//		@Override
//		public List<CombinationIdNameTypeResponseDTO> getIdNameTypeCombination() throws RuntimeException {
//			List<CombinationIdNameTypeResponseDTO> examResponse = new ArrayList<CombinationIdNameTypeResponseDTO>();
//			List<Exam> examData = examRepo.findAll();
//			examData.forEach(element->{
//				CombinationIdNameTypeResponseDTO newExam = new CombinationIdNameTypeResponseDTO();
//				Long id = element.getExamID();
//				String name = element.getExamName();
//				String type = element.getExamType();
//				String result = id + " - " + name + " - " + type;
//				newExam.setExamID(id);
//				newExam.setResult(result);
//				examResponse.add(newExam);;
//			});
//			return examResponse;
//		}

//		@Override
//		public getEicNameByExamIdResponse getEicNameByExamId(int examId) throws RuntimeException {
//			Optional<Exam> examData = examRepo.findByExamID(examId);
//			if(examData.isPresent()) {
//				Long proId = examData.get().getProjectID();
//				Optional<ProjectMaster> proData = proMaster.findByProjectID(proId);
//				if(proData.isPresent()) {
//					Long eicId = proData.get().getEicID();
//					String luName = proData.get().getProjectLUName();
//					String result = proId + " : " + luName;
//					Optional<ExaminerInformation> examInfoData = examInfo.findByExaminerID(eicId);
//					if(examInfoData.isPresent()) {
//						String first = examInfoData.get().getFirstName();
//						String last = examInfoData.get().getLastName();
//						String eicName = first + " " + last;
//						getEicNameByExamIdResponse response = new getEicNameByExamIdResponse();
//						response.setEicName(eicName);
//						response.setProjectId(proId);
//						response.setProjectLUName(luName);
//						response.setResult(result);
//						return response;
//					}
//					else {
//						return null;
//					}
//				}
//				else {
//					return null;
//				}
//			}
//			else {
//				return null;
//			}
//			
//		}
//
//		@Override
//		public String addExamInAuditCollection(int examId,String excelName,List<String> keyColumns) throws RuntimeException {
//			List<AuditColl> auditData = auditCollRepo.findAllByExamID(examId);
//			List<Long> fileSeq = new ArrayList<Long>();
//			auditData.forEach(element->{
//				long fileNo = element.getExcelSequenceNumber();
//				fileSeq.add(fileNo);
//			});
//			Collections.sort(fileSeq, Collections.reverseOrder());
//			long count = fileSeq.get(0);
//			count++;
//			LocalDateTime dateTime = java.time.LocalDateTime.now();
//			AuditColl newExam = new AuditColl();
//			List<AuditHistory> auditH = new ArrayList<AuditHistory>();
//			newExam.setExamID(examId);
//			newExam.setCurrentStatus("IMPORT COMPLETED");
//			newExam.setUpdatedBy("UI");
//			newExam.setExcelSequenceNumber(count);
//			newExam.setCurrentUpdatedDate(dateTime);
//			newExam.setKeyColumns(keyColumns);
//			newExam.setExcelSheetName(excelName);
//			newExam.setAuditHistory(auditH);
//			auditCollRepo.save(newExam);
//			return Message.BOOLEAN_RESPONSE + "DATA CREATED SUCCESSFULLY";
//		}
		
	

		@Override
		public String updateExamName(long examId, String examName) throws RuntimeException {
			Optional<Exam> data = examRepo.findByExamID(examId);
			if(data.isPresent()) {
				data.get().setExamName(examName);
				examRepo.save(data.get());
				return Message.BOOLEAN_RESPONSE + "EXAM NAME UPDATED SUCCESSFULLY";
			}
			else {
				return Message.BOOLEAN_RESPONSE + NO_DATA_FOUND;
			}
			
		}
		

		@Override
		public String deleteConsumerExam(long examId) throws RuntimeException {
			List<ConsumerLinecard> data = consumerLinecardRepo.findByExamIDAndExamSampleIDNotAndExaminerIDNot(examId,0,0);
			List<ConsumerLinecard> asignedList = data.stream().filter(lineCardObj -> (lineCardObj.getExaminerID() != 0 && lineCardObj.getExamSampleID() != 0)).collect(Collectors.toList());
			Optional<List<AuditColl>> auditCollList = auditCollRepo.findByExamIDAndCurrentStatus(examId,Constants.LOADED_TO_TARGET);
			if(CollectionUtils.isEmpty(asignedList) && CollectionUtils.isEmpty(auditCollList.get())) {
				return Message.BOOLEAN_RESPONSE + "Exam can be deleted";
			}
			else {
				return Message.BOOLEAN_RESPONSE + "Exam cannot be deleted";
				
			}
		}

		@Override
		public String addExistingField(ConsumerFields consumerFields,long examId) throws RuntimeException {
			long fieldId = consumerFields.getFieldID();
			boolean required = consumerFields.isRequired();
			String caption = consumerFields.getCaption();
			long linecardIdSecOverride = consumerFields.getSectionID();
			long reportSecIDOverride = consumerFields.getReportSectionID();
			
			Optional<Exam> examData = examRepo.findByExamID(examId);
			if(examData.isPresent()) {
				List<ExamFields> fields = examData.get().getFields();
				ExamFields existField = fields.stream().filter(element->element.getFieldID() == fieldId).findAny()
				  .orElse(null);
				if(existField == null) {
					ExamFields newFields = new ExamFields();
					newFields.setFieldID(fieldId);
					newFields.setRequired(required);
					newFields.setCaptionOverride(caption);
					newFields.setLinecardIDSecOverride(linecardIdSecOverride);
					newFields.setReportSecIDOverride(reportSecIDOverride);
					fields.add(newFields);
					examRepo.save(examData.get());				
				}	
			}
			
			return Message.BOOLEAN_RESPONSE + "Data Insert successfully";
		}

		@Override
		public List<ConsumerFields> getExistingField() throws RuntimeException {
			List<ConsumerFields> consumerFieldData = consumerFieldsRepo.findByCollName("tclDataExamSpecific");
			return consumerFieldData;
		}
		
		@Override
		public WrapUpViewResponse wrapUpCondition(long examId) throws RuntimeException {
			List<Long> combination = new ArrayList();
			List<ConsumerLinecard> consumerLinecardData = consumerLinecardRepo.findByExamSampleIDGreaterThanAndFinalConclusionIsNullAndExamID(0,examId);
			consumerLinecardData.forEach(linecardObject->{
				long examSampleId = linecardObject.getExamSampleID();
				if(examSampleId != 0) {
					Optional<ExamSamples> examSampleData = examSampleRepo.findBySampleID(examSampleId);
					if(examSampleData.isPresent()) {
						String linecardId = linecardObject.getLinecardID();
						List<DBObject> examAppData = mongoTemplate.find(new Query(Criteria.where("linecardID").is(linecardId)),DBObject.class,"ConsumerExamAppData");
						if(!CollectionUtils.isEmpty(examAppData)) {
							long examinerId = linecardObject.getExaminerID();
							Optional<ExaminerInformation> examinerInfo = examInfo.findByExaminerID(examinerId);
							Optional<ConsumerGuestExaminer> examinerGuest = guestExaminerRepo.findByExaminerID(examinerId);
							if(examinerInfo.isPresent()) {
								long examinerIDs = examinerInfo.get().getExaminerID();
								combination.add(examinerIDs);
							}
							else {
								if(examinerGuest.isPresent()) {
								long examinerIDs = examinerGuest.get().getExaminerID();
								combination.add(examinerIDs);
							}
						}
					}
				}
			}
		});
			Integer secondCount = secondView(examId);
			Integer thirdCount = thirdView(examId);
			int firstCount = combination.size();
			boolean wrapUp = false;
			if(firstCount > 0 || secondCount > 0 || thirdCount > 0) {
				wrapUp = true;
			}
			WrapUpViewResponse response = WrapUpViewResponse.builder().linecardReviewCount(firstCount).missingEICCommentsCount(secondCount).examSummaryCount(thirdCount).wrapUp(wrapUp).build();
			return response;
		}
		
		
		public Integer secondView(long examId) {
			List<Long> combination = new ArrayList();	
			List<ConsumerLinecard> consumerLinecardData = consumerLinecardRepo.findByExamSampleIDGreaterThanAndEicCommentsIsNullAndFinalConclusionNotNullAndCompletedAndExamID(0,true,examId);
			consumerLinecardData.forEach(linecardObject->{
				long examSampleId = linecardObject.getExamSampleID();
				if(examSampleId != 0) {
					Optional<ExamSamples> examSampleData = examSampleRepo.findBySampleID(examSampleId);
					if(examSampleData.isPresent()) {
						String linecardId = linecardObject.getLinecardID();
						List<DBObject> examAppData = mongoTemplate.find(new Query(Criteria.where("linecardID").is(linecardId)),DBObject.class,"ConsumerExamAppData");
						if(!CollectionUtils.isEmpty(examAppData)) {
							long examinerId = linecardObject.getExaminerID();
							Optional<ExaminerInformation> examinerInfo = examInfo.findByExaminerID(examinerId);
							Optional<ConsumerGuestExaminer> examinerGuest = guestExaminerRepo.findByExaminerID(examinerId);
							if(examinerInfo.isPresent()) {
								long examinerIDs = examinerInfo.get().getExaminerID();
								combination.add(examinerIDs);
							}
							else {
								if(examinerGuest.isPresent()) {
								long examinerIDs = examinerGuest.get().getExaminerID();
								combination.add(examinerIDs);
							}
						}
					}
				}
			}
		});
			int count = combination.size();
			return count;
			
		}
		
		public Integer thirdView(long examId) {
			List<Long> combination = new ArrayList();
			List<ConsumerLinecard> consumerLinecardData = consumerLinecardRepo.findByExamSampleIDGreaterThanAndExamIDAndCompleted(0,examId,false);
			consumerLinecardData.forEach(element->{
				long examinerId = element.getExaminerID();
				Optional<ExaminerInformation> examinerInfo = examInfo.findByExaminerID(examinerId);
				Optional<ConsumerGuestExaminer> examinerGuest = guestExaminerRepo.findByExaminerID(examinerId);
				if(examinerInfo.isPresent()) {
					long examinerIDs = examinerInfo.get().getExaminerID();
					combination.add(examinerIDs);
				}
				else {
					if(examinerGuest.isPresent()) {
						long examinerIDs = examinerGuest.get().getExaminerID();
						combination.add(examinerIDs);
					}
				}
			});
			int count = combination.size();
			return count;
			
		}

//		@Override
//		public List<ConsumerLinecardSection> getExistingFieldSectionDropDown() throws RuntimeException {
//			List<ConsumerLinecardSection> list = new ArrayList<>();
//			List<ConsumerFields> consumerFieldData = consumerFieldsRepo.findByCollName("tclDataExamSpecific");
//			consumerFieldData.forEach(element->{
//				long sectionId = element.getSectionID();
//				Optional<ConsumerLinecardSection> data = consumerLinecardSectionRepo.findBySectionID(sectionId);
//				if(data.isPresent()) {
//					list.add(data.get());
//				}
//				
//			});
//			return list;
//		}
			

}
