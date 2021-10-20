package com.wellsfargo.rarconsumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExamFields;
import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.entity.Fields;
import com.wellsfargo.rarconsumer.repository.ExamRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.repository.LinecardFieldManagementRepo;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LinecardFieldManagementDTO;
import com.wellsfargo.rarconsumer.response.MaxIDDTO;
import com.wellsfargo.rarconsumer.util.AggregationUtils;

@Service
public class LinecardFieldManagementServiceImpl implements LinecardFieldManagementService {

	@Autowired
	private LinecardFieldManagementRepo linecardFieldManagementRepo;

	@Autowired
	ExaminerSettingsRepo examinerSettingsRepo;

	@Autowired
	ExamRepo examRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ExamLinecardService linecardService;

	@Override
	public List<LinecardFieldManagementDTO> getExamFieldsBySection(Long examid, String section) {
		List<LinecardFieldManagementDTO> fieldsList = getFieldsByExam(examid);
		return fieldsList.stream().filter(currField -> currField.getSection().equalsIgnoreCase(section))
				.collect(Collectors.toList());
	}

	@Override
	public JSONStatus createLinecardField(LinecardFieldManagementDTO requestDTO, Long examinerid) {
		JSONStatus status = new JSONStatus();
		try {
			Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerid);
			if (examinerSettings.isPresent()) {
				Long lastExamID = examinerSettings.get().getLastExamID();
				Exam exam = examRepo.findFirstByExamID(lastExamID);
				String maxoperation = AggregationUtils.getMaxAggregation("fieldID");
				Aggregation aggregation = AggregationUtils.prepareAggregation(maxoperation);
				AggregationResults<MaxIDDTO> aggregationResult = mongoTemplate.aggregate(aggregation, "ConsumerFields",
						MaxIDDTO.class);
				Long uniqueID = aggregationResult.getMappedResults().get(0).getMaxID() + 1;
				ExamFields examField = new ExamFields();
				examField.setFieldID(uniqueID);
				examField.setFieldStatus("required");
				examField.setMapped(null);
				examField.setSortOrder(requestDTO.getSortOrder());
				examField.setCaptionOverride("false");
				String sectionName = requestDTO.getSection();
				Long sectionID = linecardService.getExaminerLCSections(examinerid).stream()
						.filter(currSection -> currSection.getSection().equalsIgnoreCase(sectionName)).findFirst().get()
						.getSectionID();

				Fields newField = new Fields();
				newField.setFieldID(uniqueID);
				newField.setFieldName(requestDTO.getCaption().replaceAll(" ", "").replaceAll("-", "")
						.replaceAll("%", "").replaceAll(".", ""));
				newField.setToolTip(requestDTO.getToolTip());
				newField.setCaption(requestDTO.getCaption());
				newField.setCollName("DataUtility");
				newField.setSectionID(sectionID);

				List<ExamFields> examFields = exam.getFields();
				if (examFields == null) {
					examFields = new ArrayList();
				}
				examFields.add(examField);
				exam.setFields(examFields);
				examRepo.save(exam);
				linecardFieldManagementRepo.save(newField);
				status.setStatus("created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public JSONStatus updateLinecardField(LinecardFieldManagementDTO requestDTO) {
		JSONStatus status = new JSONStatus();
		Long fieldID = requestDTO.getFieldID();
		Optional<Fields> existingField = linecardFieldManagementRepo.findByFieldID(fieldID);
		if (existingField.isPresent()) {
			Fields examFields = existingField.get();
			examFields.setToolTip(requestDTO.getToolTip());
			linecardFieldManagementRepo.save(examFields);
		}
		status.setStatus("updated");
		return status;
	}

	@Override
	public List<LinecardFieldManagementDTO> getAllExamFields(Long examinerid) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerid);
		if (examinerSettings.isPresent()) {
			Long lastExamID = examinerSettings.get().getLastExamID();
			return getFieldsByExam(lastExamID);
		}
		return null;
	}

	private List<LinecardFieldManagementDTO> getFieldsByExam(Long examID) {
		String whereoperation = AggregationUtils.getMatchOperation("examID", examID);
		String unwindoperation = AggregationUtils.getUnwindAggregation("fields");
		String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerFields", "fields.fieldID", "fieldID",
				"fielddata");
		String unwindfieldoperation = AggregationUtils.getUnwindAggregation("fielddata");
		String notemptyOperation = AggregationUtils.getNotEmptyAggregation("fielddata");
		String sectionlookupoperation = AggregationUtils.getLookupAggregation("ConsumerLinecardSection",
				"fielddata.sectionID", "sectionID", "sectiondata");
		String unwindsectionoperation = AggregationUtils.getUnwindAggregation("sectiondata");
		String projectoperation = "{$project:{'fieldID':'$fields.fieldID','fieldName':'$fielddata.fieldName','captionexam':'$fields.captionOverride','caption':'$fielddata.caption','sortOrder':'$fields.sortOrder','mapped':'$fields.mapped','status':'$fields.status','collName':'$fielddata.collName','section':'$sectiondata.section','captionOverride':'$fielddata.allowCaptionOverride','toolTip':'$fielddata.toolTip'}}";
		Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, unwindoperation, lookupoperation,
				unwindfieldoperation, notemptyOperation, sectionlookupoperation, unwindsectionoperation,
				projectoperation);
		AggregationResults<LinecardFieldManagementDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
				"ConsumerExam", LinecardFieldManagementDTO.class);
		return aggregationResult.getMappedResults();
	}

	@Override
	public JSONStatus deleteLinecardField(Long examinerid, Long fieldid) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerid);
		JSONStatus status = new JSONStatus();
		if (examinerSettings.isPresent()) {
			Long examID = examinerSettings.get().getLastExamID();
			Exam currExam = examRepo.findFirstByExamID(examID);
			List<ExamFields> filteredFields = currExam.getFields().stream()
					.filter(currField -> currField.getFieldID() != fieldid).collect(Collectors.toList());
			currExam.setFields(filteredFields);
			examRepo.save(currExam);
			status.setStatus("deleted");
		}
		return status;
	}
}
