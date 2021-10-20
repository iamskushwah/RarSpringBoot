package com.wellsfargo.rarconsumer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.response.ExamSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.SampleSummaryGridDTO;
import com.wellsfargo.rarconsumer.util.AggregationUtils;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	ExaminerSettingsRepo examinerSettingsRepo;

	@Override
	public List<ExamSummaryGridDTO> getExamSummary(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String lookupoperation = AggregationUtils.getLookupAggregation("ExaminerInformation", "examinerID", "examinerID", "examiner");
			String unwindoperation = AggregationUtils.getUnwindAggregation("examiner");
			String notEmptyOperation = AggregationUtils.getNotEmptyAggregation("examiner");
			String projectoperation = "{$project:{'examinerFullName': {$concat: ['$examiner.lastName', ' ', '$examiner.firstName']},'examinerLastName': '$examiner.lastName','examinerFirstName': '$examiner.firstName','examID': '$examID','examinerConclusionAgreeCount': {$cond: {if: {$and: [{$eq: ['$conclusion', 'Agree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerConclusionDisagreeCount': {$cond: {if: {$and: [{$eq: ['$conclusion', 'Disagree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerCompletedCount': {$cond: {if: {$and: [{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerWIPCount': {$cond: {if: {$and: [{$eq: ['$completed', false]}]},then: 1,else: 0}},'finalConclusionAgreeCount': {$cond: {if: {$and: [{$eq: ['$finalConclusion', 'Agree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'finalConclusionDisgreeCount': {$cond: {if: {$and: [{$eq: ['$finalConclusion', 'Disagree']},{$eq: ['$completed', true]}]},then: 1,else: 0}},'examinerConclusion': '$conclusion','countCompleted': '$completed','finalConclusion': '$finalConclusion','examrID': '$examrID','overturnCount': {$cond: {if: {$and: [{$ne: ['$finalConclusion', '$conclusion']},{$ne: ['$finalConclusion', '']},{$ne: ['$finalConclusion', null]},{$eq: ['$completed', true]}]},then: 1,else: 0}}}}";
			String groupoperation = "{$group:{_id:{examID:'$examID',examinerName:'$examinerFullName'},examinerCompletedCountSum:{$sum:'$examinerCompletedCount'},examinerWIPCountSum:{$sum:'$examinerWIPCount'},examinerAgreeCountSum:{$sum:'$examinerConclusionAgreeCount'},examinerDisagreeCountSum:{$sum:'$examinerConclusionDisagreeCount'},finalAgreeCountSum:{$sum:'$finalConclusionAgreeCount'},overturnCountSum:{$sum:'$overturnCount'},finalDisagreeCountSum:{$sum:'$finalDisagreeCount'}}}";
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			Aggregation aggregation = AggregationUtils.prepareAggregation(lookupoperation, whereoperation,
					unwindoperation, notEmptyOperation, projectoperation, groupoperation);
			AggregationResults<ExamSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", ExamSummaryGridDTO.class);

			return aggregationResult.getMappedResults();
		}
		return null;
	}

	@Override
	public List<SampleSummaryGridDTO> getSampleSummary(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String lookupoperation = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID",
					"sampleID", "sample");
			String unwindoperation = AggregationUtils.getUnwindAggregation("sample");
			String notemptyOperation = AggregationUtils.getNotEmptyAggregation("sample");
			String projectoperation = "{$project:{examID:'$examID',examinerID:'$examinerID',sampleName:'$sample.sampleName',subName:'$sample.sampleSubName',linecardSampled:{$literal:1},linecardRemaining:{$cond:{'if':{$and:[{$eq:['$examinerID',0]}]},then:1,else:0}},wip:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',false]}]},then:1,else:0}},linecardCompleted:{$cond:{'if':{$and:[{$ne:['$examinerID',0]},{$eq:['$completed',true]}]},then:1,else:0}},agree:{$cond:{'if':{$and:[{$eq:['$conclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},finalAgree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Agree']},{$eq:['$completed',true]}]},then:1,else:0}},disagree:{$cond:{'if':{$and:[{$eq:['$conclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},finalDisagree:{$cond:{'if':{$and:[{$eq:['$finalConclusion','Disagree']},{$eq:['$completed',true]}]},then:1,else:0}},examinerConclusion:'$conclusion',finalConclusion:'$finalConclusion',countCompleted:'$completed',active:'$sample.active',sortOrder:'$sample.sortOrder',sampleID:'$sample.sampleID',oldRemainingCalc:{$cond:{'if':{$or:[{$eq:['$completed',false]}]},then:1,else:0}}}}";
			String groupoperation = "{$group:{_id:{examId:'$examID',sampleName:'$sampleName',subName:'$subName',active:'$active',sortOrder:'$sortOrder',examSampleId:'$sampleID'},sampleCountSum:{$sum:'$linecardSampled'},linecardRemainingCountSum:{$sum:'$linecardRemaining'},linecardWIPCountSum:{$sum:'$wip'},linecardCompletedCountSum:{$sum:'$linecardCompleted'},linecardAgreedCountSum:{$sum:'$agree'},linecardFinalAgreeCountSum:{$sum:'$finalAgree'},linecardDisagreeCountSum:{$sum:'$disagree'},linecardFinalDisagreeCountSum:{$sum:'$finalDisagree'}}}";
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			Aggregation aggregation = AggregationUtils.prepareAggregation(lookupoperation, whereoperation,
					unwindoperation, notemptyOperation, projectoperation, groupoperation);
			AggregationResults<SampleSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", SampleSummaryGridDTO.class);
			return aggregationResult.getMappedResults();
		}
		return null;
	}

	@Override
	public List<LCReviewSummaryGridDTO> getLCReviewSummary(Long examinerID) throws RuntimeException {
		Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
		if (examinerSettings.isPresent()) {
			long lastExamID = examinerSettings.get().getLastExamID();
			String whereoperation = AggregationUtils.getMatchOperation("examID", lastExamID);
			String samplelookup = AggregationUtils.getLookupAggregation("ConsumerExamSample", "examSampleID", "sampleID", "examsample");
			String unwindsample = AggregationUtils.getUnwindAggregation("examsample");
			String datalookup = AggregationUtils.getLookupAggregation("ConsumerExamAppData", "linecardID", "linecardID", "lcdata");
			String unwinddata = AggregationUtils.getUnwindAggregation("lcdata");
			String examinerlookup = AggregationUtils.getLookupAggregation("ExaminerInformation", "examinerID", "examinerID", "examiner");
			String unwindexaminer = AggregationUtils.getUnwindAggregation("examiner");
			String notemptyexaminer =  AggregationUtils.getNotEmptyAggregation("examiner");
			String notemptysample =  AggregationUtils.getNotEmptyAggregation("examSampleID");
			String projectionoperation = "{$project:{'examID':'$examID','examinerID':'$examinerID','sampleName':'$examsample.sampleName','borrower1':'$lcdata.Borrower1','complete':'$completed','jobID':'$lcdata.JobID','applicationNumber':'$lcdata.ApplicationNumber','accountNumber':'$lcdata.AccountNumber','examinerFullName':{$concat:['$examiner.lastName','','$examiner.firstName']},'examinerConclusion':'$conclusion','finalConclusion':'$finalConclusion','linecardID':'$linecardID','overturn':{$cond:{if:{$and:[{$ne:['$finalConclusion','$conclusion']},{$ne:['$finalConclusion','']},{$ne:['$finalConclusion',null]},{$eq:['$completed',true]}]},then:true,else:false}},'selectedForPrinting':'$selectedForPrinting','eicReviewed':'$EICReviewed'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation,samplelookup,unwindsample,datalookup,unwinddata,examinerlookup,unwindexaminer,notemptyexaminer,notemptysample,projectionoperation);
			AggregationResults<LCReviewSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,
					"ConsumerLinecard", LCReviewSummaryGridDTO.class);
			List<LCReviewSummaryGridDTO> result = aggregationResult.getMappedResults();
			result.stream().forEach(lc->{
				lc.setApplicationNumber(AggregationUtils.getApplicationNumber(lc));
			});
			return result;
		}
		return null;
	}
}
