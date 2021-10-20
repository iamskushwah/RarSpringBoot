package com.wellsfargo.rarconsumer.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wellsfargo.rarconsumer.entity.Exam;
import com.wellsfargo.rarconsumer.repository.ExamRepo;
import com.wellsfargo.rarconsumer.response.LinecardSectionResponseDTO;
import com.wellsfargo.rarconsumer.service.ExamLinecardService;
import com.wellsfargo.rarconsumer.service.ExamWrapupService;

@Controller
@RequestMapping("/lc")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class LCPrintController {
	
	private static final Logger logger = LoggerFactory.getLogger(LCPrintController.class);
	
	@Autowired
	private ExamWrapupService wrapupService;

	@Autowired
	private ExamLinecardService examLinecardService;
	
	@Autowired
	private ExamRepo examRepo;
	
	@GetMapping("/v1/print")
	public String getLCPrintTemplate(ModelMap model,@RequestParam(value="lcid", required = true) String lcid)   throws RuntimeException {
		Document lcData = wrapupService.getLinecardData(lcid);
		HashMap<String,String> linecardData = new HashMap();
		List<Document> results = new ArrayList();
		Exam lcExam = null;
		List<LinecardSectionResponseDTO> lcSections = null;
		if(lcData!=null) {
			results = (List<Document>)lcData.get("results");
			if(results.size()>0) {
				lcData = results.get(0);
				lcData.entrySet().forEach(entry->{
					if(entry.getKey()!="lcdata") {
						linecardData.put(entry.getKey(),entry.getValue()!=null ? entry.getValue().toString() : "");	
					}
				});
				lcExam = examRepo.findFirstByExamID(lcData.getLong("examID"));
				lcData = (Document)lcData.get("lcdata");
				lcData.entrySet().forEach(entry->{
					linecardData.put(entry.getKey(),entry.getValue()!=null ? entry.getValue().toString() : "");	
				});
				lcSections = examLinecardService.getLCSectionsByLinecardID(lcid);
				lcSections = lcSections.stream().filter(lc->((!lc.getSuppressSection()) && (lc.getDisplayTemplate()!=null || lc.getPrintTemplate()!=null))).sorted((lc1,lc2)->lc1.getReportOrderOverride()!=null && lc2.getReportOrderOverride() !=null ? lc1.getReportOrderOverride().compareTo(lc2.getReportOrderOverride()) : 0).collect(Collectors.toList());
			}
		}
		model.addAttribute("lcData",linecardData);
		model.addAttribute("lcExam",lcExam);
		model.addAttribute("lcSections",lcSections);
		return "lcprint";
	}
}
