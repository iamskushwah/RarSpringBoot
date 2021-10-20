package com.wellsfargo.rarconsumer.service;

import java.util.List;

import com.wellsfargo.rarconsumer.request.ExamManagementResourceRequestDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceNameDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceRoleDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceSamplesDTO;
import com.wellsfargo.rarconsumer.response.JSONStatus;

public interface ExamManagementResourceService {

	List<ExamManagementResourceDTO> getExamManagementResource(long examinerID);

	JSONStatus deleteExamManagementResource(long userExamID);

	JSONStatus updateExamManagementResource(ExamManagementResourceRequestDTO requestDTO);

	List<ExamManagementResourceNameDTO> getAllExamResourceNames();

	List<ExamManagementResourceRoleDTO> getAllExamResourceRoles();

	List<ExamManagementResourceSamplesDTO> getAllExamResourceSamples();

	JSONStatus createExamManagementResourceService(ExamManagementResourceRequestDTO requestDTO, long examinerid);
}
