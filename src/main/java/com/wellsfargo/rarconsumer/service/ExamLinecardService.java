package com.wellsfargo.rarconsumer.service;

import java.util.List;


import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LCReviewSummaryGridDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionRequestDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionResponseDTO;
import com.wellsfargo.rarconsumer.response.LinecardSectionTemplateResponseDTO;


public interface ExamLinecardService {
	List<LinecardSectionResponseDTO> getExaminerLCSections(Long examinerID);
	List<LinecardSectionResponseDTO> getLCSectionsByLinecardID(String linecardID);
	JSONStatus updateLCSection(Long sectionid, LinecardSectionResponseDTO payload);
	Long getLastExamID(Long examinerID);
	String getExamInstructions(Long examinerID);
	JSONStatus updateExamInstructions(Long examinerID, String instructions);
	List<LCReviewSummaryGridDTO> getLinecardToReassign(Long examinerID);
	JSONStatus updateLCSectionTemplate(LinecardSectionTemplateResponseDTO payload,Long examinerid);
	JSONStatus createLCSection(LinecardSectionRequestDTO payload,Long examinerid);
	List<LinecardSectionResponseDTO> getLCSectionsByExamID(Long examID);
}
