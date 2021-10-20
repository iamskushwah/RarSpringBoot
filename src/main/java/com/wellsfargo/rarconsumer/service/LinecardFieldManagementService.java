package com.wellsfargo.rarconsumer.service;

import java.util.List;

import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.LinecardFieldManagementDTO;

public interface LinecardFieldManagementService {

    JSONStatus createLinecardField(LinecardFieldManagementDTO requestDTO,Long examinerID);

    JSONStatus updateLinecardField(LinecardFieldManagementDTO requestDTO);

    List<LinecardFieldManagementDTO> getAllExamFields(Long examinerid);
    
    List<LinecardFieldManagementDTO> getExamFieldsBySection(Long examid, String section);

	JSONStatus deleteLinecardField(Long examid, Long fieldid);
}
