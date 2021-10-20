package com.wellsfargo.rarconsumer.util;

import java.util.Date;

import org.springframework.data.mongodb.core.aggregation.Aggregation;

import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;

public class AggregationUtils {

	public static Aggregation prepareAggregation(String... operations) {
		GenericAggregationOperation aggregations[] = new GenericAggregationOperation[operations.length];
		for(int i = 0;i<operations.length;i++) {
			String currOperation = operations[i];
			GenericAggregationOperation currAggregation = GenericAggregationUtils.addAggregateStage(currOperation);
			aggregations[i]=currAggregation;
		}
		Aggregation aggregation = Aggregation.newAggregation(aggregations);
		return aggregation;
	}
	public static String getLookupAggregation(String mappingCollection,String srcField,String destField,String alias) {
		String operation = "{ $lookup: {from: '"+mappingCollection+"',localField: '"+srcField+"',foreignField: '"+destField+"',as: '"+alias+"'}}";
		return operation;
	}
	public static String getUnwindAggregation(String alias) {
		String operation = "{$unwind: {path: '$"+alias+"',preserveNullAndEmptyArrays: true}}";
		return operation;
	}
	public static String getNotEmptyAggregation(String alias) {
		String operation = "{$match: {'"+alias+"':{$ne:null}}}";
		return operation;
	}
	public static String getMatchOperation(String string, long lastExamID) {
		return "{$match: {'examID': " + lastExamID + "}}";
	}
	public static String getMaxAggregation(String column) {
		return "{$group:{_id:'',maxID:{$max:'$"+column+"'}}}";
	}
	public static String getApplicationNumber(LCReviewSummaryGridDTO lc) {
		Long jobID = lc.getJobID(); 
		String applicationNumber = lc.getApplicationNumber();
		String accountNumber = lc.getAccountNumber();
		if(jobID==null || jobID==0) {
			if(applicationNumber!=null && applicationNumber.trim().length()>0 && applicationNumber.indexOf("/")==-1) {
				return applicationNumber;
			} else {
				return accountNumber!=null ? accountNumber : "";
			}
		} else {
			return lc.getJobID().toString();
		}
	}
	public static Boolean isLCWithoutFinalConclusion(LCReviewSummaryGridDTO lc) {
		if(lc.getComplete()!=null && lc.getComplete().equals(true)) {
			String finalConclusion = lc.getFinalConclusion();
			if(finalConclusion==null) {
				return true;
			} else {
				return finalConclusion.trim().length()==0;						
			}
		} else {
			return false;	
		}
	}
	public static Boolean isLCWithoutEICComments(LCReviewSummaryGridDTO lc) {
		Long examSampleID = lc.getExamSampleID()==null ? 0 : lc.getExamSampleID();
		String eicComments = lc.getEicComments()==null ? "" : lc.getEicComments().trim();
		String finalConclusion = lc.getFinalConclusion()==null? "" : lc.getFinalConclusion().trim();
		Boolean examinerCompleted = lc.getComplete()==null?false:lc.getComplete();
		if(examSampleID!=0 && eicComments=="" && finalConclusion!="" && examinerCompleted) {
			return true;
		} else if(examSampleID!=0 && eicComments.equals("0") && finalConclusion!="" && examinerCompleted){
			return true;			
		} else {
			return false;
		}
	}
	public static Boolean isLCExaminerCompleted(LCReviewSummaryGridDTO lc) {
		Long examSampleID = lc.getExamSampleID()==null ? 0 : lc.getExamSampleID();
		Boolean examinerCompleted = lc.getComplete()==null?false:lc.getComplete();
		Date completionDate = lc.getCompletionDate();
		if(examSampleID!=0 && examinerCompleted==false && completionDate!=null) {
			return true;
		} else {
			return false;
		}
	}
}
