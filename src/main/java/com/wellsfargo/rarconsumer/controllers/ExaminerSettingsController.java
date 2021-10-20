package com.wellsfargo.rarconsumer.controllers;

import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
import com.wellsfargo.rarconsumer.service.ExaminerSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposes end-point for Exam_ID Message
 *
 * @author u813333
 *
 */
@RestController
@RequestMapping("/api/examid/v1")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ExaminerSettingsController {

    @Autowired
    ExaminerSettingsService examinerSettingsService;

    private static final Logger logger = LoggerFactory.getLogger(ExaminerSettingsController.class);

    @PutMapping("/updatelastexaminersettings")
    public ResponseEntity<ResponseApiDTO> updateExaminerSettings(@RequestParam(value="examinerID", required = true) long examinerID,
                                                           @RequestParam(value="lastExamID", required = true) long lastExamID) throws RuntimeException {

        return examinerSettingsService.updateLastExamInExaminerSettings( examinerID, lastExamID );
    }

}
