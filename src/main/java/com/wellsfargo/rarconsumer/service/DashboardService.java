package com.wellsfargo.rarconsumer.service;

import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.wellsfargo.rarconsumer.response.ExamSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.SampleSummaryGridDTO;

public interface DashboardService {

	List<ExamSummaryGridDTO> getExamSummary(Long examinerID) throws RuntimeException;
	
	List<SampleSummaryGridDTO> getSampleSummary(Long examinerID) throws RuntimeException;

	List<LCReviewSummaryGridDTO> getLCReviewSummary(Long examinerID) throws RuntimeException;

}
