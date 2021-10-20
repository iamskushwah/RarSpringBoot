package com.wellsfargo.rarconsumer.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import com.wellsfargo.rarconsumer.entity.ExamSampleBuild;
import com.wellsfargo.rarconsumer.entity.ExamSamples;
import com.wellsfargo.rarconsumer.request.ConsumerLineCardDeleteRequest;
import com.wellsfargo.rarconsumer.request.ExamSamplesRequestDTO;
import com.wellsfargo.rarconsumer.response.ExamSamplesResponseDTO;
import com.wellsfargo.rarconsumer.response.ExamSamplesResponsePageableDTO;

        public interface ExamSamplesService {

            public ExamSamplesResponsePageableDTO getAll(long examId, String excelSheetName);

            public ExamSamplesResponsePageableDTO getExamSamples(int page, int size) throws RuntimeException;

            public ResponseEntity saveExamSample(ExamSamplesRequestDTO examSampleRequestDTO);

            public ResponseEntity deleteExamSample(String id);

            public ExamSamplesResponseDTO loadExamSampleById(String id);

            public ResponseEntity updateExamSample(ExamSamples examSample);

            public ResponseEntity patchExamSample(ExamSamples examSample);
            
            public String deleteBuild(long sampleId,String comment) throws RuntimeException;
               
            public List<ExamSampleBuild> getExamSampleBuild(long sampleId) throws RuntimeException;
            
            public String deleteConsumerLinecard(ConsumerLineCardDeleteRequest deleteRequest) throws RuntimeException;
            
            public List<Map<String, Object>> getExamAppData(long examId,long sampleId) throws RuntimeException;
        
            public String dataInsertOnBuild(long sampleId,long sampleSize,String updatedBy) throws RuntimeException;
        
            public String consumerLinecardDeleteCheck(String linecardId) throws RuntimeException;
      
            public String examSampleDeleteCheck(long sampleId) throws RuntimeException;
        
        }

