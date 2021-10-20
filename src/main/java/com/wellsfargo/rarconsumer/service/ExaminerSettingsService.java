package com.wellsfargo.rarconsumer.service;

import org.springframework.http.ResponseEntity;

public interface ExaminerSettingsService {

    public ResponseEntity updateLastExamInExaminerSettings(long examinerID, long lastExamID );
}
