package com.wellsfargo.rarconsumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExamLinecardSections;
import com.wellsfargo.rarconsumer.entity.ExaminerInformation;
import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.entity.LineCard;
import com.wellsfargo.rarconsumer.entity.LinecardSections;
import com.wellsfargo.rarconsumer.repository.ExamRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerInformationRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.repository.LineCardRepo;
import com.wellsfargo.rarconsumer.repository.LineCardSectionsRepo;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionRequestDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionResponseDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionTemplateResponseDTO;
import com.wellsfargo.rarconsumer.response.MaxIDDTO;
import com.wellsfargo.rarconsumer.util.AggregationUtils;

@Service
public class ExamLinecardServiceImpl implements ExamLinecardService {

	@Autowired
	ExaminerSettingsRepo examinerSettingsRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	ExamRepo examRepo;

	@Autowired
	LineCardRepo linecardRepo;

	@Autowired
	LineCardSectionsRepo lcSectionsRepo;

	@Autowired
	ExaminerInformationRepo examinerRepo;

	@Override
	public List<LinecardSectionResponseDTO> getLCSectionsByExamID(Long examID) {
		String whereoperation = "{$match: {'examID': " + examID + "}}";
		String unwindoperation = AggregationUtils.getUnwindAggregation("linecardSections");
		String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerLinecardSection",
				"linecardSections.sectionID", "sectionID", "sectionDetails");
		String unwindSectionOperation = AggregationUtils.getUnwindAggregation("sectionDetails");
		String notemptyOperation = AggregationUtils.getNotEmptyAggregation("sectionDetails");
		String projectoperation = "{$project:{displayTemplate:'$sectionDetails.displayTemplate',printTemplate:'$sectionDetails.printTemplate',sectionID:'$sectionDetails.sectionID',examID:'$examID',section:'$sectionDetails.section',secDesc:'$sectionDetails.secDesc',suppressSection:'$linecardSections.suppressSection',linecardOrderOverride:'$linecardSections.linecardOverride',reportOrderOverride:'$linecardSections.reportOrderOverride',pgBrkOverride:'$linecardSections.pgBrkOverride'}}";

		Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, unwindoperation, lookupoperation,
				unwindSectionOperation, notemptyOperation, projectoperation);
		AggregationResults<LinecardSectionResponseDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
				"ConsumerExam", LinecardSectionResponseDTO.class);
		return aggregationResult.getMappedResults();
	}

	@Override
	public List<LinecardSectionResponseDTO> getExaminerLCSections(Long examinerID) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			Long lastExamID = examinerSettings.get().getLastExamID();
			return getLCSectionsByExamID(lastExamID);
		}
		return null;
	}

	@Override
	public JSONStatus updateLCSection(Long sectionid, LinecardSectionResponseDTO payload) {
		JSONStatus result = new JSONStatus();
		Long examID = payload.getExamID();
		Exam exam = examRepo.findFirstByExamID(examID);
		exam.getLinecardSections().forEach(currSection -> {
			if (currSection.getSectionID() == sectionid) {
				//currSection.setLinecardOrderOverride(payload.getLinecardOrderOverride());
				//Field name has been changed
				currSection.setLinecardOverride(payload.getLinecardOrderOverride());
				currSection.setPgBrkOverride(payload.getPgBrkOverride());
				//currSection.setSuppressSection(payload.getSuppressSection());
				//Field name has been changed
				currSection.setSuppress(payload.getSuppressSection());
			}
		});
		examRepo.save(exam);
		result.setStatus("Updated");
		return result;
	}

	@Override
	public Long getLastExamID(Long examinerID) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		return examinerSettings.isPresent() ? examinerSettings.get().getLastExamID() : 0;
	}

	@Override
	public JSONStatus updateExamInstructions(Long examinerID, String instructions) {
		JSONStatus result = new JSONStatus();
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			Long examID = examinerSettings.get().getLastExamID();
			Exam exam = examRepo.findFirstByExamID(examID);
			exam.setInstructions(instructions);
			examRepo.save(exam);
			result.setStatus("updated");
		} else {
			result.setStatus("failed updating");
		}
		return result;
	}

	@Override
	public String getExamInstructions(Long examinerID) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			Long examID = examinerSettings.get().getLastExamID();
			Exam exam = examRepo.findFirstByExamID(examID);
			return exam.getInstructions();
		}
		return "";
	}

	@Override
	public List<LCReviewSummaryGridDTO> getLinecardToReassign(Long examinerID) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			Long lastExamID = examinerSettings.get().getLastExamID();
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			String samplelookup = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID",
					"sampleID", "examsample");
			String unwindsample = AggregationUtils.getUnwindAggregation("examsample");
			String datalookup = AggregationUtils.getLookupAggregation("ConsumerExamAppData", "linecardID", "linecardID",
					"lcdata");
			String unwinddata = AggregationUtils.getUnwindAggregation("lcdata");
			String examinerlookup = AggregationUtils.getLookupAggregation("ExaminerInformation", "examinerID",
					"examinerID", "examiner");
			String unwindexaminer = AggregationUtils.getUnwindAggregation("examiner");
			String notemptyexaminer = AggregationUtils.getNotEmptyAggregation("completed");
			String notcompletedoperation = "{$match: {'completed':false}}";
			String currexmaineroperation = "{$match: {'examinerID':" + examinerID + "}}";
			String projectionoperation = "{$project:{'examID':'$examID','examinerID':'$examinerID','sampleName':'$examsample.sampleName','borrower1':'$lcdata.Borrower1','complete':'$completed','jobID':'$lcdata.JobID','applicationNumber':'$lcdata.ApplicationNumber','accountNumber':'$lcdata.AccountNumber','examinerFullName':{$concat:['$examiner.lastName','','$examiner.firstName']},'examinerConclusion':'$conclusion','finalConclusion':'$finalConclusion','linecardID':'$linecardID','overturn':{$cond:{if:{$and:[{$ne:['$finalConclusion','$conclusion']},{$ne:['$finalConclusion','']},{$ne:['$finalConclusion',null]},{$eq:['$completed',true]}]},then:true,else:false}},'selectedForPrinting':'$selectedForPrinting','eicReviewed':'$EICReviewed'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, samplelookup, unwindsample,
					datalookup, unwinddata, examinerlookup, unwindexaminer, notemptyexaminer, notcompletedoperation,
					currexmaineroperation, projectionoperation);
			AggregationResults<LCReviewSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LCReviewSummaryGridDTO.class);
			List<LCReviewSummaryGridDTO> result = aggregationResult.getMappedResults();
			result.stream().forEach(lc -> {
				lc.setApplicationNumber(AggregationUtils.getApplicationNumber(lc));
			});
			return result;
		}
		return null;
	}

	@Override
	public JSONStatus updateLCSectionTemplate(LinecardSectionTemplateResponseDTO payload, Long examinerid) {
		Optional<ExaminerInformation> examiner = examinerRepo.findByExaminerID(examinerid);
		JSONStatus status = new JSONStatus("");
		LinecardSections section = lcSectionsRepo.findFirstBySectionID(payload.getSectionID());
		if (section != null) {
			if (payload.getSaveForPrint()) {
				section.setPrintTemplate(payload.getConfiguration());
			}
			if (payload.getSaveForDisplay()) {
				section.setDisplayTemplate(payload.getConfiguration());
			}
			section.setUpdatedDate(new Date());
			section.setUpdatedBy(
					examiner.isPresent() ? examiner.get().getFirstName() + " " + examiner.get().getLastName() : "");
			lcSectionsRepo.save(section);
			status.setStatus("Template Saved successfull");
			return status;
		}
		return null;
	}

	@Override
	public JSONStatus createLCSection(LinecardSectionRequestDTO payload, Long examinerid) {
		JSONStatus status = new JSONStatus("");
		Optional<ExaminerInformation> examiner = examinerRepo.findByExaminerID(examinerid);
		String maxOperation = AggregationUtils.getMaxAggregation("sectionID");
		Aggregation aggregation = AggregationUtils.prepareAggregation(maxOperation);
		AggregationResults<MaxIDDTO> aggregationResult = mongoTemplate.aggregate(aggregation, "ConsumerLinecardSection",
				MaxIDDTO.class);
		Long sectionID = aggregationResult.getMappedResults().get(0).getMaxID() + 1;
		LinecardSections section = new LinecardSections();
		section.setSectionID(sectionID);
		section.setSection(payload.getSectionName());
		section.setSecDesc(payload.getSectionDesc());
		section.setUpdatedDate(new Date());
		section.setUpdatedBy(
				examiner.isPresent() ? examiner.get().getFirstName() + " " + examiner.get().getLastName() : "");
		lcSectionsRepo.save(section);
		Exam currExam = examRepo.findFirstByExamID(payload.getExamID());
		if (currExam != null) {
			List<ExamLinecardSections> sections = currExam.getLinecardSections();
			ExamLinecardSections newSection = new ExamLinecardSections();
			newSection.setSectionID(sectionID);
			newSection.setSuppress(false);
			newSection.setPgBrkOverride(false);
			newSection.setLinecardOverride(new Long(1));
			newSection.setReportOrderOverride(new Long(1));
			if (sections == null) {
				sections = new ArrayList();
			}
			sections.add(newSection);
			currExam.setLinecardSections(sections);
			examRepo.save(currExam);
			status.setStatus("Section Created for Exam");
			return status;
		}
		return null;
	}

	@Override
	public List<LinecardSectionResponseDTO> getLCSectionsByLinecardID(String linecardID) {
		LineCard lc = linecardRepo.findFirstByLinecardID(linecardID);
		if (lc != null) {
			Long examID = lc.getExamID();
			List<LinecardSectionResponseDTO> results= getLCSectionsByExamID(examID);
			return results;
		}
		return null;
	}
}
