package com.wellsfargo.rarconsumer.service;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.http.ResponseEntity;

import com.wellsfargo.rarconsumer.response.ExamSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.SampleSummaryGridDTO;

public interface ExamWrapupService {
	List<LCReviewSummaryGridDTO> getLCWithoutFinalConclusion(Long examinerID) throws RuntimeException;
	List<LCReviewSummaryGridDTO> getLCWithoutEICComments(Long examinerID) throws RuntimeException;
	List<LCReviewSummaryGridDTO> getLCExaminerCompleted(Long examinerID) throws RuntimeException;
	List<ExamSummaryGridDTO> getWIPSummary(Long examinerID) throws RuntimeException;
	List<SampleSummaryGridDTO> getIncompleteSamples(Long examinerID) throws RuntimeException;
	List<SampleSummaryGridDTO> getRemainingSamples(Long examinerID) throws RuntimeException;
	JSONStatus clearWIPLC(long examinerID);
	JSONStatus clearMISAssignedLC(long examinerID);
	JSONStatus deleteSample(long examSampleID,long sampleCount,long examinerid);
	JSONStatus getNewLinecard(long examinerid);
	Document getLinecardData(String lcid);
	JSONStatus updateLinecardData(String examinerid, Map<String, Object> payload);
	JSONStatus exportLinecardData(String examinerid, String lid, String payload);
}
