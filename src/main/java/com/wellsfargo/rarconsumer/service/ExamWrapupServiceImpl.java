package com.wellsfargo.rarconsumer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.mongodb.client.FindIterable;
import com.wellsfargo.rarconsumer.entity.ConsumerExamAppData;
import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.entity.ExamSampleBuild;
import com.wellsfargo.rarconsumer.entity.ExamSamples;
import com.wellsfargo.rarconsumer.entity.ExaminerInformation;
import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.entity.LineCard;
import com.wellsfargo.rarconsumer.repository.ConsumerExamAppDataRepo;
import com.wellsfargo.rarconsumer.repository.ExamRepo;
import com.wellsfargo.rarconsumer.repository.ExamSampleBuildRepo;
import com.wellsfargo.rarconsumer.repository.ExamSamplesRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerInformationRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.repository.LineCardRepo;
import com.wellsfargo.rarconsumer.response.ExamSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.SampleSummaryGridDTO;
import com.wellsfargo.rarconsumer.util.AggregationUtils;

@Service
public class ExamWrapupServiceImpl implements ExamWrapupService {


	@Value("${mountPath:\\\\\\\\MOST821nvs1.wellsfargo.com\\\\RAR_D_WORKPAPERS\\\\}")
	private String mountPath;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	ExaminerSettingsRepo examinerSettingsRepo;

	@Autowired
	ExaminerInformationRepo examinerRepo;

	@Autowired
	ExamSamplesRepo examSamplesRepo;

	@Autowired
	ExamRepo examRepo;
	
	@Autowired
	LineCardRepo lineCardRepo;

	@Autowired
	ExamSampleBuildRepo examSampleBuildRepo;

	@Autowired
	ConsumerExamAppDataRepo examDataRepo;
	
	@Override
	public List<LCReviewSummaryGridDTO> getLCWithoutFinalConclusion(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
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
			String notemptyexaminer = AggregationUtils.getNotEmptyAggregation("examiner");
			String notemptysample = AggregationUtils.getNotEmptyAggregation("examSampleID");
			String projectionoperation = "{$project:{'examID':'$examID','examinerID':'$examinerID','sampleName':'$examsample.sampleName','borrower1':'$lcdata.Borrower1','complete':'$completed','jobID':'$lcdata.JobID','applicationNumber':'$lcdata.ApplicationNumber','accountNumber':'$lcdata.AccountNumber','examinerFullName':{$concat:['$examiner.lastName','','$examiner.firstName']},'examinerConclusion':'$conclusion','finalConclusion':'$finalConclusion','linecardID':'$linecardID','overturn':{$cond:{if:{$and:[{$ne:['$finalConclusion','$conclusion']},{$ne:['$finalConclusion','']},{$ne:['$finalConclusion',null]},{$eq:['$completed',true]}]},then:true,else:false}},'selectedForPrinting':'$selectedForPrinting','eicReviewed':'$EICReviewed'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, samplelookup, unwindsample,
					datalookup, unwinddata, examinerlookup, unwindexaminer, notemptyexaminer, notemptysample,
					projectionoperation);
			AggregationResults<LCReviewSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LCReviewSummaryGridDTO.class);
			List<LCReviewSummaryGridDTO> result = aggregationResult.getMappedResults();
			result = result.stream().filter(lc -> AggregationUtils.isLCWithoutFinalConclusion(lc))
					.collect(Collectors.toList());
			result.stream().forEach(lc -> {
				lc.setApplicationNumber(AggregationUtils.getApplicationNumber(lc));
			});
			return result;
		}
		return null;
	}

	@Override
	public List<LCReviewSummaryGridDTO> getLCWithoutEICComments(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
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
			String notemptyexaminer = AggregationUtils.getNotEmptyAggregation("examiner");
			String notemptysample = AggregationUtils.getNotEmptyAggregation("examSampleID");
			String projectionoperation = "{$project:{'examID':'$examID','examinerID':'$examinerID','sampleName':'$examsample.sampleName','borrower1':'$lcdata.Borrower1','complete':'$completed','jobID':'$lcdata.JobID','applicationNumber':'$lcdata.ApplicationNumber','accountNumber':'$lcdata.AccountNumber','examinerFullName':{$concat:['$examiner.lastName','','$examiner.firstName']},'examinerConclusion':'$conclusion','finalConclusion':'$finalConclusion','linecardID':'$linecardID','overturn':{$cond:{if:{$and:[{$ne:['$finalConclusion','$conclusion']},{$ne:['$finalConclusion','']},{$ne:['$finalConclusion',null]},{$eq:['$completed',true]}]},then:true,else:false}},'selectedForPrinting':'$selectedForPrinting','eicReviewed':'$EICReviewed'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, samplelookup, unwindsample,
					datalookup, unwinddata, examinerlookup, unwindexaminer, notemptyexaminer, notemptysample,
					projectionoperation);
			AggregationResults<LCReviewSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LCReviewSummaryGridDTO.class);
			List<LCReviewSummaryGridDTO> result = aggregationResult.getMappedResults();
			result.stream().filter(lc -> AggregationUtils.isLCWithoutEICComments(lc)).forEach(lc -> {
				lc.setApplicationNumber(AggregationUtils.getApplicationNumber(lc));
			});
			return result;
		}
		return null;
	}

	@Override
	public List<LCReviewSummaryGridDTO> getLCExaminerCompleted(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
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
			String notemptyexaminer = AggregationUtils.getNotEmptyAggregation("examiner");
			String notemptysample = AggregationUtils.getNotEmptyAggregation("examSampleID");
			String projectionoperation = "{$project:{'completionDate':'$completionDate','examID':'$examID','examinerID':'$examinerID','sampleName':'$examsample.sampleName','borrower1':'$lcdata.Borrower1','complete':'$completed','jobID':'$lcdata.JobID','applicationNumber':'$lcdata.ApplicationNumber','accountNumber':'$lcdata.AccountNumber','examinerFullName':{$concat:['$examiner.lastName','','$examiner.firstName']},'examinerConclusion':'$conclusion','finalConclusion':'$finalConclusion','linecardID':'$linecardID','overturn':{$cond:{if:{$and:[{$ne:['$finalConclusion','$conclusion']},{$ne:['$finalConclusion','']},{$ne:['$finalConclusion',null]},{$eq:['$completed',true]}]},then:true,else:false}},'selectedForPrinting':'$selectedForPrinting','eicReviewed':'$EICReviewed'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, samplelookup, unwindsample,
					datalookup, unwinddata, examinerlookup, unwindexaminer, notemptyexaminer, notemptysample,
					projectionoperation);
			AggregationResults<LCReviewSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LCReviewSummaryGridDTO.class);
			List<LCReviewSummaryGridDTO> result = aggregationResult.getMappedResults();
			result = result.stream().filter(lc -> AggregationUtils.isLCExaminerCompleted(lc))
					.collect(Collectors.toList());
			result.stream().forEach(lc -> {
				lc.setApplicationNumber(AggregationUtils.getApplicationNumber(lc));
			});
			return result;
		}
		return null;
	}

	@Override
	public List<ExamSummaryGridDTO> getWIPSummary(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String lookupoperation = AggregationUtils.getLookupAggregation("ExaminerInformation", "examinerID",
					"examinerID", "examiner");
			String unwindoperation = AggregationUtils.getUnwindAggregation("examiner");
			String notEmptyOperation = AggregationUtils.getNotEmptyAggregation("examiner");
			String projectoperation = "{$project:{'examinerFullName': {$concat: ['$examiner.lastName', ' ', '$examiner.firstName']},'examinerLastName': '$examiner.lastName','examinerFirstName': '$examiner.firstName','examID': '$examID','examinerConclusionAgreeCount': {$cond: {if: {$and: [{$eq: ['$conclusion', 'Agree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerConclusionDisagreeCount': {$cond: {if: {$and: [{$eq: ['$conclusion', 'Disagree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerCompletedCount': {$cond: {if: {$and: [{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerWIPCount': {$cond: {if: {$and: [{$eq: ['$completed', false]}]},then: 1,else: 0}},'finalConclusionAgreeCount': {$cond: {if: {$and: [{$eq: ['$finalConclusion', 'Agree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'finalConclusionDisgreeCount': {$cond: {if: {$and: [{$eq: ['$finalConclusion', 'Disagree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerConclusion': '$conclusion','countCompleted': '$completed','finalConclusion': '$finalConclusion','examrID': '$examrID','overturnCount': {$cond: {if: {$and: [{$ne: ['$finalConclusion', '$conclusion']},{$ne: ['$finalConclusion', '']},{$ne: ['$finalConclusion', null]},{$eq: ['$completed', true]}]},then: 1,else: 0}}}}";
			String groupoperation = "{$group:{_id:{examID:'$examID',examinerName:'$examinerFullName'},examinerCompletedCountSum:{$sum:'$examinerCompletedCount'},examinerWIPCountSum:{$sum:'$examinerWIPCount'},examinerAgreeCountSum:{$sum:'$examinerConclusionAgreeCount'},examinerDisagreeCountSum:{$sum:'$examinerConclusionDisagreeCount'},finalAgreeCountSum:{$sum:'$finalConclusionAgreeCount'},overturnCountSum:{$sum:'$overturnCount'},finalDisagreeCountSum:{$sum:'$finalDisagreeCount'}}}";
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			Aggregation aggregation = AggregationUtils.prepareAggregation(lookupoperation, whereoperation,
					unwindoperation, notEmptyOperation, projectoperation, groupoperation);
			AggregationResults<ExamSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", ExamSummaryGridDTO.class);
			List<ExamSummaryGridDTO> result = aggregationResult.getMappedResults();
			return result.stream().filter(summary -> summary.getExaminerWIPCountSum() > 0).collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<SampleSummaryGridDTO> getIncompleteSamples(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID",
					"sampleID", "sample");
			String unwindoperation = AggregationUtils.getUnwindAggregation("sample");
			String notemptyOperation = AggregationUtils.getNotEmptyAggregation("sample");
			String projectoperation = "{$project:{deleted:'$sample.deleted',examID:'$examID',examinerID:'$examinerID',sampleName:'$sample.sampleName',subName:'$sample.sampleSubName',linecardSampled:{$literal:1},linecardRemaining:{$cond:{'if':{$and:[{$eq:['$examinerID',0]}]},then:1,else:0}},wip:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',false]}]},then:1,else:0}},linecardCompleted:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',true]}]},then:1,else:0}},agree:{$cond:{'if':{$and:[{$eq:['$conclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},finalAgree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},disagree:{$cond:{'if':{$and:[{$eq:['$conclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},finalDisagree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},examinerConclusion:'$conclusion',finalConclusion:'$finalConclusion',countCompleted:'$completed',active:'$sample.active',sortOrder:'$sample.sortOrder',sampleID:'$sample.sampleID',oldRemainingCalc:{$cond:{'if':{$or:[{$eq:['$completed',false]}]},then:1,else:0}}}}";
			String groupoperation = "{$group:{_id:{examId:'$examID',sampleName:'$sampleName',subName:'$subName',active:'$active',sortOrder:'$sortOrder',examSampleId:'$sampleID'},sampleCountSum:{$sum:'$linecardSampled'},linecardRemainingCountSum:{$sum:'$linecardRemaining'},linecardWIPCountSum:{$sum:'$wip'},linecardCompletedCountSum:{$sum:'$linecardCompleted'},linecardAgreedCountSum:{$sum:'$agree'},linecardFinalAgreeCountSum:{$sum:'$finalAgree'},linecardDisagreeCountSum:{$sum:'$disagree'},linecardFinalDisagreeCountSum:{$sum:'$finalDisagree'}}}";
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			String notdeletedoperation = "{$match: {'deleted': false}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(lookupoperation, whereoperation,
					unwindoperation, notemptyOperation, notdeletedoperation, projectoperation, groupoperation);
			AggregationResults<SampleSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", SampleSummaryGridDTO.class);
			List<SampleSummaryGridDTO> result = aggregationResult.getMappedResults();
			result = result.stream()
					.filter(sample -> sample.getSampleCountSum() == sample.getLinecardRemainingCountSum())
					.collect(Collectors.toList());
			return result;
		}
		return null;
	}

	@Override
	public List<SampleSummaryGridDTO> getRemainingSamples(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID",
					"sampleID", "sample");
			String unwindoperation = AggregationUtils.getUnwindAggregation("sample");
			String notemptyOperation = AggregationUtils.getNotEmptyAggregation("sample");
			String projectoperation = "{$project:{deleted:'$sample.deleted',examID:'$examID',examinerID:'$examinerID',sampleName:'$sample.sampleName',subName:'$sample.sampleSubName',linecardSampled:{$literal:1},linecardRemaining:{$cond:{'if':{$and:[{$eq:['$examinerID',0]}]},then:1,else:0}},wip:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',false]}]},then:1,else:0}},linecardCompleted:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',true]}]},then:1,else:0}},agree:{$cond:{'if':{$and:[{$eq:['$conclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},finalAgree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},disagree:{$cond:{'if':{$and:[{$eq:['$conclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},finalDisagree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},examinerConclusion:'$conclusion',finalConclusion:'$finalConclusion',countCompleted:'$completed',active:'$sample.active',sortOrder:'$sample.sortOrder',sampleID:'$sample.sampleID',oldRemainingCalc:{$cond:{'if':{$or:[{$eq:['$completed',false]}]},then:1,else:0}}}}";
			String groupoperation = "{$group:{_id:{examId:'$examID',sampleName:'$sampleName',subName:'$subName',active:'$active',sortOrder:'$sortOrder',examSampleId:'$sampleID'},sampleCountSum:{$sum:'$linecardSampled'},linecardRemainingCountSum:{$sum:'$linecardRemaining'},linecardWIPCountSum:{$sum:'$wip'},linecardCompletedCountSum:{$sum:'$linecardCompleted'},linecardAgreedCountSum:{$sum:'$agree'},linecardFinalAgreeCountSum:{$sum:'$finalAgree'},linecardDisagreeCountSum:{$sum:'$disagree'},linecardFinalDisagreeCountSum:{$sum:'$finalDisagree'}}}";
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			String notdeletedoperation = "{$match: {'deleted': false}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(lookupoperation, whereoperation,
					unwindoperation, notemptyOperation, notdeletedoperation, projectoperation, groupoperation);
			AggregationResults<SampleSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", SampleSummaryGridDTO.class);
			List<SampleSummaryGridDTO> result = aggregationResult.getMappedResults();
			result = result.stream()
					.filter(sample -> sample.getSampleCountSum() != sample.getLinecardRemainingCountSum()
							&& sample.getLinecardRemainingCountSum() > 0)
					.collect(Collectors.toList());
			return result;
		}
		return null;
	}

	@Override
	public JSONStatus clearWIPLC(long examinerID) {
		JSONStatus status = new JSONStatus();
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			long emptyExaminer = 0;
			List<LineCard> linecardList = lineCardRepo.findAllByExamIDAndExaminerIDAndCompleted(lastExamID, emptyExaminer, false);
			linecardList.forEach(lc->{
				lc.setExaminerID(0);
				lineCardRepo.save(lc);
			});
			status.setStatus("updated");
		}
		return status;
	}

	@Override
	public JSONStatus clearMISAssignedLC(long examinerID) {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		JSONStatus status = new JSONStatus();
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			List<ExaminerInformation> activeExaminers = examinerRepo.findAllByEmployeeGroupIgnoreCaseLikeAndActive("systems", true);
			List<Long> examinerIDs = new ArrayList();
			activeExaminers.stream().forEach(examiner -> examinerIDs.add(examiner.getExaminerID()));
			List<LineCard> linecards = lineCardRepo.findAllByExamIDAndExaminerIDIn(lastExamID, examinerIDs);
			linecards.forEach(lc->{
				lc.setExaminerID(0);
				lineCardRepo.save(lc);
			});
			List<ExaminerSettings> examinerSettingsData = examinerSettingsRepo.findAllByLastExamIDAndExaminerIDIn(lastExamID, examinerIDs);
			examinerSettingsData.forEach(setting-> {
				setting.setLastLinecardID("");
				examinerSettingsRepo.save(setting);
			});
			status.setStatus("updated");
			return status;
		}
		return null;
	}
	
	@Override
	public JSONStatus deleteSample(long examSampleID,long sampleCount,long examinerid) {
		JSONStatus status = new JSONStatus();
		List<ExamSamples> examSamples = examSamplesRepo.findAllBySampleID(examSampleID);
		examSamples.forEach(sample->{
			sample.setDeleted(true);
			sample.setActive(false);
			sample.setComment("Sample Deleted");
			examSamplesRepo.save(sample);
		});
		Date currDate = new Date();
		String maxBuildID = AggregationUtils.getMaxAggregation("sampleBuildID");
		Aggregation aggregation = AggregationUtils.prepareAggregation(maxBuildID);
		AggregationResults<Long> aggregationResult = mongoTemplate.aggregate(aggregation,"ConsumerExamSampleBuild",Long.class);
		Long sampleBuildID= aggregationResult.getMappedResults().get(0).longValue();
		ExamSampleBuild sampleBuild = new ExamSampleBuild();
		sampleBuild.setSampleID(examSampleID);
		sampleBuild.setSampleSize(sampleCount);
		sampleBuild.setMode("Delete");
		sampleBuild.setUpdatedBy(""+examinerid);
		sampleBuild.setEditBy(""+examinerid);
		sampleBuild.setBuildDate(currDate);
		sampleBuild.setUpdatedDate(currDate);
		sampleBuild.setSampleBuildID(sampleBuildID);
		examSampleBuildRepo.save(sampleBuild);
		List<LineCard> linecards = lineCardRepo.findAllByExamSampleID(examSampleID);
		linecards.forEach(lc->{
			lc.setExaminerID(0);
			lineCardRepo.save(lc);
		});
		status.setStatus("deleted");
		return status;
	}

	@Override
	@Transactional
	public JSONStatus getNewLinecard(long examinerid) {
		JSONStatus status = new JSONStatus();
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerid);
		if (examinerSettings.isPresent()) {
			ExaminerSettings settings = new ExaminerSettings();
			long lastExamID = settings.getLastExamID();
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID","sampleID", "sample");
			String unwindoperation = AggregationUtils.getUnwindAggregation("sample");
			String notemptyOperation = AggregationUtils.getNotEmptyAggregation("sample");
			String activesampleOperation = "{$match: {'examsample.active': true}}";
			String unassignedOperation = "{$match:{$or:[{'examinerID':{'$exists':false},'examinerID':'','examinerID':0}]}}";
			String sortOperation = "{$sort:{'linecardID':1,'examsample.sortOrder':1}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation, lookupoperation,unwindoperation,notemptyOperation,activesampleOperation,unassignedOperation,sortOperation);
			AggregationResults<LineCard> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LineCard.class);
			List<LineCard> data = aggregationResult.getMappedResults();
			if(data.size()>0) {
				String lcid = data.get(0).getLinecardID();
				LineCard newLinecard = lineCardRepo.findFirstByLinecardID(lcid);
				newLinecard.setExaminerID(examinerid);
				newLinecard.setAssignmentDate(new Date());
				lineCardRepo.save(newLinecard);
				settings.setLastLinecardID(lcid);
				examinerSettingsRepo.save(settings);
				status.setStatus("linecard assigned");
			} else {
				status.setStatus("linecard not available");
			}
		}
		return status;
	}

	@Override
	public Document getLinecardData(String lcid) {
		String lcFilterOperation = "{$match: {'linecardID': '"+lcid+"'}}";
		String lcAppData = AggregationUtils.getLookupAggregation("ConsumerExamAppData", "linecardID","linecardID", "lcdata");
		String unwindoperation = AggregationUtils.getUnwindAggregation("lcdata");
		String notemptyOperation = AggregationUtils.getNotEmptyAggregation("lcdata");
		Aggregation aggregation = AggregationUtils.prepareAggregation(lcFilterOperation, lcAppData,unwindoperation,notemptyOperation);
		AggregationResults<LineCard> aggregationResult = mongoTemplate.aggregate(aggregation,
				"ConsumerLinecard", LineCard.class);
		return aggregationResult.getRawResults();
	}

	@Override
	public JSONStatus updateLinecardData(String examinerid, Map<String, Object> payload) {
		JSONStatus status = new JSONStatus();
		String linecardID = (String)payload.get("linecardID");
		if(linecardID!=null && linecardID.trim()!="") {
			Bson criteriaValue = BsonDocument.parse("{'linecardID': '"+linecardID+"'}");
			FindIterable<Document> documentList  = mongoTemplate.getCollection("ConsumerExamAppData").find(criteriaValue);
			Document existingDoc = documentList.first();
			payload.entrySet().forEach(entry->{
				String key = entry.getKey();
				if(!("conclusion".equals(key) || "finalConclusion".equals(key) || "completed".equals(key) || "EICComments".equals(key) || "summary".equals(key))) {
					existingDoc.put(entry.getKey(),entry.getValue());
				}
			});
			mongoTemplate.getCollection("ConsumerExamAppData").findOneAndReplace(criteriaValue, existingDoc);
			
			LineCard linecard = lineCardRepo.findFirstByLinecardID(linecardID);			
			linecard.setConclusion((String)payload.get("conclusion"));
			linecard.setFinalConclusion((String)payload.get("finalConclusion"));
			linecard.setCompleted((Boolean)payload.get("completed"));
			linecard.setEICComments((String)payload.get("EICComments"));
			linecard.setSummary((String)payload.get("summary"));
			lineCardRepo.save(linecard);
			
			status.setStatus("Updated");
			return status;
		}
		return null;
	}

	@Override
	public JSONStatus exportLinecardData(String examinerid, String lid, String payload) {
		JSONStatus status = new JSONStatus();
		LineCard lc = lineCardRepo.findFirstByLinecardID(lid);
		if(lc!=null) {
			long examID = lc.getExamID();
			Exam exam = examRepo.findFirstByExamID(examID);
			Optional<ConsumerExamAppData> appData = examDataRepo.findFirstByLinecardID(lid);			
			String folderPath = exam.isArchived() ? exam.getLinecardDirFinal(): exam.getLinecardDir();
			try {
				folderPath=folderPath.substring(3);
				folderPath = mountPath+folderPath;
				folderPath = folderPath.replace("\\","/");
				File folderDirs = new File(folderPath);
				if(!folderDirs.exists()) {
					folderDirs.mkdirs(); 	
				}
				Document lcData = this.getLinecardData(lid);
				HashMap<String,String> linecardData = new HashMap();
				List<Document> results = new ArrayList();
				if(lcData!=null) {
					results = (List<Document>)lcData.get("results");
					if(results.size()>0) {
						lcData = results.get(0);
						lcData.entrySet().forEach(entry->{
							if(entry.getKey()!="lcdata") {
								linecardData.put(entry.getKey(),entry.getValue()!=null ? entry.getValue().toString() : "");	
							}
						});
						lcData = (Document)lcData.get("lcdata");
						lcData.entrySet().forEach(entry->{
							linecardData.put(entry.getKey(),entry.getValue()!=null ? entry.getValue().toString() : "");	
						});
					}
				}
				ConverterProperties properties = new ConverterProperties();
				MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.PROJECTION);
				properties.setMediaDeviceDescription(mediaDeviceDescription);
				PdfWriter writer = new PdfWriter(folderPath+linecardData.get("Borrower1")+"-"+linecardData.get("ApplicationNumber")+".pdf");
				PdfDocument pdf = new PdfDocument(writer);
				pdf.setDefaultPageSize(new PageSize(750,768));
				HtmlConverter.convertToPdf(payload, pdf, properties);
			    pdf.close();
			    writer.flush();
			    writer.close();
			    status.setStatus("Exported Succesfully to "+folderPath);
				return status;
			} catch(Exception e) {
				e.printStackTrace();
				status.setStatus(e.getMessage() + folderPath);
				return status;
			}
		}
		status.setStatus("Error while Exporting PDF");
		return status;
	}
}
