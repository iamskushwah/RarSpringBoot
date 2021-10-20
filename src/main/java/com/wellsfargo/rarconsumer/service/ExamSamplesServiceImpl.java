package com.wellsfargo.rarconsumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wellsfargo.rarconsumer.SpringConfig;

import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mongodb.DBObject;
import com.wellsfargo.rarconsumer.entity.ConsumerLinecard;
import com.wellsfargo.rarconsumer.entity.ExamSampleBuild;
import com.wellsfargo.rarconsumer.entity.ExamSamples;
import com.wellsfargo.rarconsumer.exception.BadRequestException;
import com.wellsfargo.rarconsumer.repository.ConsumerExamAppDataRepo;
import com.wellsfargo.rarconsumer.repository.ConsumerLinecardRepo;
import com.wellsfargo.rarconsumer.repository.ExamSampleBuildRepo;
import com.wellsfargo.rarconsumer.repository.ExamSamplesRepo;
import com.wellsfargo.rarconsumer.request.ConsumerLineCardDeleteRequest;
import com.wellsfargo.rarconsumer.request.ExamSamplesRequestDTO;
import com.wellsfargo.rarconsumer.response.ExamSamplesResponseDTO;
import com.wellsfargo.rarconsumer.response.ExamSamplesResponsePageableDTO;
import com.wellsfargo.rarconsumer.response.Message;

@Service
public class ExamSamplesServiceImpl implements ExamSamplesService {

	@Autowired
	private ExamSamplesRepo examSamplesRepo;
	
	@Autowired
	private ExamSampleBuildRepo examSamplesBuildRepo;
	
	@Autowired
	private ConsumerLinecardRepo consumerLinecardRepo;
	
	@Autowired
	private ConsumerExamAppDataRepo consumerExamAppDataRepo;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public ExamSamplesResponsePageableDTO getAll(long examId, String excelSheetName) {

		List<ExamSamplesResponseDTO> examResponse = new ArrayList<ExamSamplesResponseDTO>();
		Sort sort = new Sort(Sort.Direction.DESC , "sampleID");
		List<ExamSamples> All = examSamplesRepo.findAllByExamIDAndDeletedAndLOBCriteria(examId,false,excelSheetName,sort);
		
		for (ExamSamples element : All) {
			examResponse.add(setExamSamplesResponse(element));
		}

		return ExamSamplesResponsePageableDTO.builder().result(examResponse).build();
	}

	public ExamSamplesResponsePageableDTO getExamSamples(int page, int size) throws RuntimeException {

		List<ExamSamplesResponseDTO> examResponse = new ArrayList<ExamSamplesResponseDTO>();

		Pageable pageable = PageRequest.of(page, size);
		Page<ExamSamples> data = examSamplesRepo.findAll(pageable);

		if (data == null) {
			return null;
		}
		for (ExamSamples element : data.getContent()) {
			examResponse.add(setExamSamplesResponse(element));
		}

		return ExamSamplesResponsePageableDTO.builder().result(examResponse).pageable(data.getPageable())
				.totalPages(data.getTotalPages()).totalElements(data.getTotalElements()).build();
	}

	public ResponseEntity saveExamSample(ExamSamplesRequestDTO examSampleRequestDTO) {
		try {
			if (validateExamSampleRequestDTO(examSampleRequestDTO)) {
					Query query = new Query();
					query.with(new Sort(Sort.Direction.DESC, "SampleID"));
					query.limit(1);
					ExamSamples maxObject = mongoTemplate.findOne(query, ExamSamples.class);
					 Long sampleId = maxObject.getSampleID();
					 sampleId++;
				if(validateCriteria(examSampleRequestDTO.getCriteria())) {
				ExamSamples examSample = new ExamSamples();
				examSample.setSampleID(sampleId);
				examSample.setCriteria(examSampleRequestDTO.getCriteria());
				examSample.setExamID(examSampleRequestDTO.getExamID());
				examSample.setLOBCriteria(examSampleRequestDTO.getLOBCriteria());
				examSample.setSampleName(examSampleRequestDTO.getSampleName());
			    examSample.setSampleSubName(examSampleRequestDTO.getSampleSubName());
				examSample.setSortOrder(examSampleRequestDTO.getSortOrder());
				examSamplesRepo.save(examSample);
				return ResponseEntity.status(201).body("created");
				}else {
					return ResponseEntity.badRequest().body("Invalid Criteria.Please validate your criteria.");
				}
			} else {
				return ResponseEntity.badRequest().body("Validation failure");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Error");

		}

	}

	public ResponseEntity deleteExamSample(String id) {

		try {
			if (id != null && !id.isEmpty()) {

				examSamplesRepo.deleteById(id);

				return ResponseEntity.ok().body("deleted");
			} else {
				return ResponseEntity.badRequest().body("Validation failure");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Error");

		}
	}

	public ExamSamplesResponseDTO loadExamSampleById(String id) {

		Optional<ExamSamples> examSample = examSamplesRepo.findById(id);
		return setExamSamplesResponse(examSample.get());

	}

	@Override
	public ResponseEntity updateExamSample(ExamSamples examSample) {

		try {
			if (validateExamSamples(examSample)) {
				if (validateCriteria(examSample.getCriteria())) {
				examSamplesRepo.save(examSample);
				return ResponseEntity.ok().body("updated");
				}else {
				return ResponseEntity.badRequest().body("Invalid Criteria.Please enter valid criteria.");	
				}
			} else {
				return ResponseEntity.badRequest().body("Validation failure");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Error");

		}

	}

	private boolean validateExamSamples(ExamSamples examSample) {
		return examSample != null && examSample.getCriteria() != null && !examSample.getCriteria().isEmpty()
				&& examSample.getExamID() != 0 && examSample.getExamID() > 0 && examSample.getLOBCriteria() != null
				&& !examSample.getLOBCriteria().isEmpty() && examSample.getSampleName() != null
				&& !examSample.getSampleName().isEmpty() && examSample.getSortOrder() != 0;
	}

	private boolean validateExamSampleRequestDTO(ExamSamplesRequestDTO examSampleRequestDTO) {
		return examSampleRequestDTO != null && examSampleRequestDTO.getCriteria() != null
				&& !examSampleRequestDTO.getCriteria().isEmpty() && examSampleRequestDTO.getExamID() != 0
				&& examSampleRequestDTO.getExamID() > 0 && examSampleRequestDTO.getLOBCriteria() != null
				&& !examSampleRequestDTO.getLOBCriteria().isEmpty() && examSampleRequestDTO.getSampleName() != null
				&& !examSampleRequestDTO.getSampleName().isEmpty() && examSampleRequestDTO.getSortOrder() != 0;
	}

	private ExamSamplesResponseDTO setExamSamplesResponse(ExamSamples examSamplesData) {

		ExamSamplesResponseDTO examSamplesResponse = ExamSamplesResponseDTO.builder().id(examSamplesData.getId())
				.sampleID(examSamplesData.getSampleID()).sampleName(examSamplesData.getSampleName())
				.sampleSubName(examSamplesData.getSampleSubName()).LOBCriteria(examSamplesData.getLOBCriteria())
				.examID(examSamplesData.getExamID()).sortOrder(examSamplesData.getSortOrder())
				.criteria(examSamplesData.getCriteria()).build();

		return examSamplesResponse;
	}

	@Override
	public ResponseEntity patchExamSample(ExamSamples patchSample) {
		ExamSamples currSample = examSamplesRepo.findFirstBysampleID(patchSample.getSampleID());
		if (patchSample.getSampleName() != null && patchSample.getSampleName().trim() != "") {
			currSample.setSampleName(patchSample.getSampleName());
		}
		if (patchSample.getSampleSubName() != null) {
			currSample.setSampleSubName(patchSample.getSampleSubName());
		}
		if (patchSample.getSortOrder() > 0) {
			currSample.setSortOrder(patchSample.getSortOrder());
		}
		if (!StringUtils.isEmpty(patchSample.getLOBCriteria())) {
			currSample.setLOBCriteria(patchSample.getLOBCriteria());
		}
		if (!StringUtils.isEmpty(patchSample.getCriteria())) {
			if(validateCriteria(patchSample.getCriteria())){
				currSample.setCriteria(patchSample.getCriteria());
			}else {
				return ResponseEntity.badRequest().body("Invalid Criteria.Please enter valid criteria.");
			}
		}
		examSamplesRepo.save(currSample);
		return ResponseEntity.ok().body(currSample);
	}


	@Override
	public String deleteBuild(long  sampleId, String comment) throws RuntimeException {
//		List<Long> sampleBuildIdList = new ArrayList<Long>();
//		List<ExamSampleBuild> data = examSamplesBuildRepo.findAll();
//		data.forEach(e->{
//			Long id = e.getSampleBuildID();
//			sampleBuildIdList.add(id);
//		});
//		 Collections.sort(sampleBuildIdList, Collections.reverseOrder());
//		 Long sampleBuildID = sampleBuildIdList.get(0);
//		 sampleBuildID++;
//		 
			Query query = new Query();
			query.with(new Sort(Sort.Direction.DESC, "SampleBuildID"));
			query.limit(1);
			ExamSampleBuild maxObject = mongoTemplate.findOne(query, ExamSampleBuild.class);
			 Long sampleBuildID = maxObject.getSampleBuildID();
			 sampleBuildID++;
			 
		 Date currDate = new Date();
		 
		 ExamSampleBuild newExam = new ExamSampleBuild();
		 newExam.setSampleID(sampleId);
		 newExam.setBuildDate(currDate);
		 newExam.setSampleBuildID(sampleBuildID);
		 newExam.setMode("Delete");
		 newExam.setUpdatedDate(currDate);
		 newExam.setUpdatedBy("UI");
		 newExam.setComment(comment);
		 newExam.setEditBy("UI");
		 newExam.setSampleSize(0L);
		 examSamplesBuildRepo.save(newExam);

		 List<ConsumerLinecard> linecardData = consumerLinecardRepo.findByExamSampleID(sampleId);
			linecardData.forEach(element->{
				element.setExamSampleID(0);
				element.setUpdatedDate(currDate);
				consumerLinecardRepo.saveAll(linecardData);
			});
				
			return Message.BOOLEAN_RESPONSE + "DATA UPDATED SUCCESSFULLY";	
	}


	
	@Override
	public List<ExamSampleBuild> getExamSampleBuild(long sampleId) throws RuntimeException {
	List<ExamSampleBuild> sampleBuildData = examSamplesBuildRepo.findBySampleID(sampleId);
	return sampleBuildData;
	
	
	
	}

	@Override
	public String deleteConsumerLinecard(ConsumerLineCardDeleteRequest deleteRequest) throws RuntimeException {
		//Optional<ConsumerLinecard> data = consumerLinecardRepo.findByLinecardID(linecardId);
		if(StringUtils.isEmpty(deleteRequest.getComment())) {
			return Message.BOOLEAN_RESPONSE + "No comments found.";
		}
		Optional<List<ConsumerLinecard>> deletedLineCardIds = consumerLinecardRepo.findByLinecardIDIn(deleteRequest.getLinecardIDs());
		if(deletedLineCardIds.isPresent()) {
			deletedLineCardIds.get().forEach(data->{
			Date currDate = new Date();
			data.setExamSampleID(0);
			data.setUpdatedDate(currDate);
			data.setDeleteComment(deleteRequest.getComment());
			consumerLinecardRepo.save(data);
			});
			return Message.BOOLEAN_RESPONSE + "DATA UPDATED SUCCESSFULLY";
		}
		else {
			return Message.BOOLEAN_RESPONSE + "DATA NOT FOUND";
		}
	}

	@Override
	public List<Map<String, Object>> getExamAppData(long examId, long sampleId) throws RuntimeException {
		List<Map<String, Object>> examResponse = new ArrayList<Map<String, Object>>();
		List<ConsumerLinecard> data = consumerLinecardRepo.findByExamSampleID(sampleId);
		data.forEach(element->{
			String linecardId = element.getLinecardID();
			Map<String,Object> lineCardMap = new HashMap<String,Object>();
			List<DBObject> examAppData = mongoTemplate.find(new Query(Criteria.where("linecardID").is(linecardId)),DBObject.class,"ConsumerExamAppData");
			if(!CollectionUtils.isEmpty(examAppData)) {
			Document lineCardDetails = (Document) examAppData.get(0);
			lineCardDetails.keySet().forEach(lineCardItem ->{
				if(!lineCardItem.equalsIgnoreCase("_id") && !lineCardItem.equalsIgnoreCase("_class"))
				lineCardMap.put(lineCardItem,lineCardDetails.get(lineCardItem));
			});
			lineCardMap.put("exam_id", Long.toString(examId));
			lineCardMap.put("exam_sample_id", Long.toString(sampleId));
			examResponse.add(lineCardMap);
			}
		});
		return examResponse;
	}

	@Autowired
	private SpringConfig springConfig;
	
	@Override
	@Transactional
	public String dataInsertOnBuild(long sampleId, long sampleSize,String updatedBy) throws RuntimeException {
			Query query = new Query();
			query.with(new Sort(Sort.Direction.DESC, "SampleBuildID"));
			query.limit(1);
			ExamSampleBuild maxObject = mongoTemplate.findOne(query, ExamSampleBuild.class);
			 Long sampleBuildID = maxObject.getSampleBuildID();
			 sampleBuildID++;
		 
		 Date currDate = new Date();
		 ExamSampleBuild newExam = new ExamSampleBuild();
		 newExam.setSampleID(sampleId);
		 newExam.setBuildDate(currDate);
		 newExam.setSampleBuildID(sampleBuildID);
		 newExam.setMode("Build");
		 newExam.setUpdatedDate(currDate);
		 newExam.setUpdatedBy(updatedBy);
		 newExam.setEditBy("UI");
		 newExam.setSampleSize(sampleSize);
		 examSamplesBuildRepo.save(newExam);
		 Optional<ExamSamples> sampleData = examSamplesRepo.findBySampleID(sampleId);
		 if(sampleData.isPresent()) {
			 List<Map<String,Object>> lineCardList = new ArrayList<>();

			 String criteria = sampleData.get().getCriteria();
			 long examId = sampleData.get().getExamID();
			 String excelSheetName = sampleData.get().getLOBCriteria();
			 
			 List<Document> examAppData= new ArrayList<>();
			 try {
			 BsonDocument criteriaDocument = BsonDocument.parse(criteria);
			 springConfig.getCollectionData("ConsumerExamAppData").find(criteriaDocument).into(examAppData);
			 }catch(Exception ex) {
				 throw new BadRequestException("Some error occured while retrieving data.Please validate your criteria");
			 }
			 if(!CollectionUtils.isEmpty(examAppData)) {
						examAppData.forEach(examAppDataObj->{
							Document lineCardDetails = (Document) examAppDataObj;
							Map<String,Object> lineCardMap = new HashMap<String,Object>();
							lineCardDetails.keySet().forEach(lineCardItem ->{
								lineCardMap.put(lineCardItem,lineCardDetails.get(lineCardItem));
							});
							
							String linecardId = lineCardMap.get("linecardID").toString();
							Optional<ConsumerLinecard> linecardData = consumerLinecardRepo.findByLinecardIDAndExamIDAndSourceDataFile(linecardId,examId,excelSheetName);
							if(linecardData.isPresent()) {
								if(checkNotAssignedSample(lineCardMap.get("linecardID").toString())) {
									lineCardList.add(lineCardMap);
								}				
							}
						});
						List<Map<String, Object>> linecardSortedList = lineCardList.stream().limit(sampleSize).collect(Collectors.toList());
						linecardSortedList.forEach(linecardItem->{
							Object linecardId = linecardItem.get("linecardID");
							String id = linecardId.toString();
							Optional<ConsumerLinecard> consumerLinecardObject = consumerLinecardRepo.findByLinecardID(id);
							if(consumerLinecardObject.isPresent()) {
								consumerLinecardObject.get().setExamSampleID(sampleId);
								consumerLinecardRepo.save(consumerLinecardObject.get());
							}
						});
						sampleData.get().setActive(true);
						examSamplesRepo.save(sampleData.get());
			}else {
				throw new BadRequestException("No records found for the given criteria.");
			}
					
		 }
		 
		return Message.BOOLEAN_RESPONSE + "DATA CREATED SUCCESSFULLY";
	}
	
	private boolean checkNotAssignedSample(String lineCardId) {
		Optional<ConsumerLinecard> consumerLinecardObject = consumerLinecardRepo.findByLinecardID(lineCardId);
		if(consumerLinecardObject.isPresent()) {
			if(consumerLinecardObject.get().getExamSampleID() == 0)
				return true;
			else
				return false;
		}
		return false;
	}
	
//	private Query generateBuildCriteria(String[] arrOfStr) {
//		Query mongoCriteria = new Query();
//		List<String> criteriaList = Arrays.asList(arrOfStr);
//		criteriaList.forEach(inputCriteria->{
//			if(inputCriteria.contains("=")) {
//				String[] predicates = inputCriteria.split("=");
//				String columnName = getKey(predicates[0]);
//				String columnValue = getValue(predicates[1]);
////				Criteria equalCriteria = new Criteria();
////				equalCriteria.and(columnName).is(columnValue);
//				//mongoCriteria.addCriteria(Criteria.where(columnName).is(columnValue));)
//				mongoCriteria.addCriteria(Criteria.where(columnName).is(columnValue));
//			}
//			if(inputCriteria.contains("not in")) {
//				String[] predicates = inputCriteria.split(" not in ");
//				String columnName = getKey(predicates[0]);
//				List<String> columnValue = getListValue(predicates[1]);
////				Criteria equalCriteria = new Criteria();
////				equalCriteria.and(columnName).not().in(columnValue);
////				mongoCriteria.add(equalCriteria);
//				mongoCriteria.addCriteria(Criteria.where(columnName).not().in(columnValue));
//			}
//			if(inputCriteria.contains("in") && !inputCriteria.contains("not in")) {
//				String[] predicates = inputCriteria.split(" in ");
//				String columnName = getKey(predicates[0]);
//				List<String> columnValue = getListValue(predicates[1]);
////				Criteria equalCriteria = new Criteria();
////				equalCriteria.and(columnName).in(columnValue);
////				mongoCriteria.add(equalCriteria);
//				mongoCriteria.addCriteria(Criteria.where(columnName).in(columnValue));
//			}
//		});
//		return mongoCriteria;
//	}
//	
//	private String getKey(String keyString) {
//		return keyString.replaceAll(" ","").replaceAll("\r\n","").split("\\.")[1];
//	}
//	
//	private String getValue(String valueString) {
//		return valueString.replaceAll("\\'","").replaceAll("\r\n","");
//	}
//	
//	private List<String> getListValue(String valueString) {
//		String replacedValue = valueString.replaceAll("\\'","").replaceAll("\r\n   ","").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\r\n  ","");
//		List<String> inValues = Arrays.asList(replacedValue.split("\\s*,\\s*"));
//		return inValues;
//	}
	
	private boolean validateCriteria(String criteria) {
		 List<Document> examAppData= new ArrayList<>();
		 try {
		 BsonDocument criteriaDocument = BsonDocument.parse(criteria);
		 springConfig.getCollectionData("ConsumerExamAppData").find(criteriaDocument).into(examAppData);
		 return true;
		 }catch(Exception ex) {
			 return false;
		 }
	}

	@Override
	public String consumerLinecardDeleteCheck(String linecardId) throws RuntimeException {
		Optional<ConsumerLinecard> data = consumerLinecardRepo.findByLinecardID(linecardId);
		if(!(data.get().getExamSampleID() == 0)) {
			if(data.get().getExaminerID() == 0) {
				return Message.BOOLEAN_RESPONSE + "Line card can be deleted";
			}
			else {
				return Message.BOOLEAN_RESPONSE + "Line card cannot be deleted";
			}
		}
		else {
			return Message.BOOLEAN_RESPONSE + "Line card can be deleted";	
		}
	}

	@Override
	public String examSampleDeleteCheck(long sampleId) throws RuntimeException {
//		List<ConsumerLinecard> data = consumerLinecardRepo.findByExamSampleIDAndExaminerIDNot(sampleId, 0);
		List<ConsumerLinecard> data = consumerLinecardRepo.findByExamSampleID(sampleId);
		if(CollectionUtils.isEmpty(data)) {
			return Message.BOOLEAN_RESPONSE + "Exam sample can be deleted";
		}
		else {
			return Message.BOOLEAN_RESPONSE + "Exam sample cannot be deleted";
		}
	}

	
}
