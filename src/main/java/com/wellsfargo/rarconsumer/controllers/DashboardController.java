package com.wellsfargo.rarconsumer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
import com.wellsfargo.rarconsumer.service.DashboardService;
import com.wellsfargo.rarconsumer.util.ResponseUtility;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class DashboardController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private DashboardService service;


	@GetMapping("/v1/examsummary")
	public ResponseEntity<ResponseApiDTO> getExamSummary( @RequestParam(value="examinerid", required = true) Long examinerID)   throws RuntimeException {
		return ResponseUtility.createSuccessResponse(service.getExamSummary(examinerID));
	}
	
	@GetMapping("/v1/samplesummary")
	public ResponseEntity<ResponseApiDTO> getSampleSummary(@RequestParam(value="examinerid", required = true) Long examinerID)   throws RuntimeException {
		return ResponseUtility.createSuccessResponse(service.getSampleSummary(examinerID));
	}
	
	@GetMapping("/v1/lcsummary")
	public ResponseEntity<ResponseApiDTO> getLCReviewSummary(@RequestParam(value="examinerid", required = true) Long examinerID)   throws RuntimeException {
		return ResponseUtility.createSuccessResponse(service.getLCReviewSummary(examinerID));
	}
}
