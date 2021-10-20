package com.wellsfargo.rarconsumer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.response.JSONStatus;

@Service
public class ExaminerSettingsServiceImpl implements ExaminerSettingsService  {

    @Autowired
    ExaminerSettingsRepo examinerSettingsRepo;

    @Override
    public ResponseEntity updateLastExamInExaminerSettings(long examinerID, long lastExamID) {
    	JSONStatus status = new JSONStatus();
        try{
            if(examinerID != 0 && lastExamID != 0){
            	Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
            	ExaminerSettings currExaminerSetting = new ExaminerSettings();
            	if(examinerSettings.isPresent()) {
            		currExaminerSetting = examinerSettings.get();
            		currExaminerSetting.setLastExamID(lastExamID);
            	} else {
            		currExaminerSetting.setExaminerID(examinerID);
            		currExaminerSetting.setLastExamID(lastExamID);
            	}
        		examinerSettingsRepo.save(currExaminerSetting);
        		status.setStatus("updated");
            } else{
            	status.setStatus("Validation failure");
            }
        }catch(Exception e){
        	e.printStackTrace();
        	status.setStatus("Internal Error");
        }
        return ResponseEntity.ok().body(status);
    }
}

