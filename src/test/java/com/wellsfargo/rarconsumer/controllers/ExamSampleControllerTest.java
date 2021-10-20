//package com.wellsfargo.rarconsumer.controllers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wellsfargo.rarconsumer.entity.ExamSamples;
//import com.wellsfargo.rarconsumer.request.ExamSamplesRequestDTO;
//import com.wellsfargo.rarconsumer.response.ExamSamplesResponseDTO;
//import com.wellsfargo.rarconsumer.response.ExamSamplesResponsePageableDTO;
//import com.wellsfargo.rarconsumer.service.ExamSamplesService;
//import com.wellsfargo.rarconsumer.service.ExamSamplesServiceImpl;
//
//
//@WebMvcTest
//@ExtendWith(MockitoExtension.class)
//public class ExamSampleControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private EICController examSampleController;
//
//    @Mock
//    private ExamSamplesService examSamplesService;
//    private static ObjectMapper mapper = new ObjectMapper();
//
//    @Before
//    public void setup() {
//        examSamplesService = Mockito.mock(ExamSamplesServiceImpl.class);
//        examSampleController = new EICController();
//        ReflectionTestUtils.setField(examSampleController, "examSamplesService", examSamplesService);
//    }
//
//    @Test
//    public void testGetExamSamples() throws Exception {
//
//        Mockito.when(examSamplesService.getAll()).thenReturn(generateMockExamSamples());
//
//        ExamSamplesResponsePageableDTO responseEntity = examSampleController.getExamSamples();
//
//        Assert.assertTrue(responseEntity != null);
//        Assert.assertTrue(responseEntity.getResult() != null && responseEntity.getResult().size() > 0);
//        ExamSamplesResponseDTO examSample = responseEntity.getResult().get(0);
//    }
//
//    private  ExamSamplesResponsePageableDTO generateMockExamSamples(){
//
//        List<ExamSamples> examSampleList = new ArrayList<ExamSamples>();
//        ExamSamples examSamples = new ExamSamples();
//        examSamples.setId("1");
//        examSamples.setActive(true);
//        examSamples.getSampleName();
//        examSamples.setCriteria("3");
//        examSamples.setExamID(2);
//        examSamples.setLOBCriteria("e");
//        examSampleList.add(examSamples);
//        List<ExamSamplesResponseDTO> examResponse = new ArrayList<ExamSamplesResponseDTO>();
//
//        for(ExamSamples element: examSampleList) {
//            examResponse.add(setExamSamplesResponse(element));
//        }
//
//        return ExamSamplesResponsePageableDTO.builder().result(examResponse).build();
//    }
//
//    private ExamSamplesResponseDTO setExamSamplesResponse(ExamSamples examSamplesData) {
//
//        ExamSamplesResponseDTO examSamplesResponse = ExamSamplesResponseDTO.builder().id(examSamplesData.getId()).
//                sampleID(examSamplesData.getSampleID()).sampleName(examSamplesData.getSampleName())
//                .sampleSubName(examSamplesData.getSampleSubName()).LOBCriteria(examSamplesData.getLOBCriteria())
//                .examID(examSamplesData.getExamID()).sortOrder(examSamplesData.getSortOrder()).criteria(examSamplesData.getCriteria()).build();
//
//        return examSamplesResponse;
//    }
//
//    @Test
//    public void testCreateExamSamples() throws Exception {
//
//        ExamSamplesRequestDTO examSample = new ExamSamplesRequestDTO();
//        examSample.setCriteria("1");
//        examSample.setActive(false);
//
//        Mockito.when(examSamplesService.saveExamSample(examSample)).thenReturn(ResponseEntity.ok("created"));
//        ResponseEntity response=examSampleController.createExamSamples(examSample);
//
//        Assert.assertEquals(200,response.getStatusCode().value());
//
//    }
//
//    @Test
//    public void testDeletedById() throws Exception {
//
//        Mockito.when(examSamplesService.deleteExamSample(Mockito.anyString())).thenReturn(ResponseEntity.ok("deleted"));
//        ResponseEntity response=examSampleController.deleteById("1");
//        Assert.assertEquals(200,response.getStatusCode().value());
//    }
//
//    @Test
//    public void testUpdateExamSample() throws Exception {
//        ExamSamples examSample = new ExamSamples();
//        examSample.setActive(true);
//        examSample.setSampleName("goutham");
//        examSample.setSampleSubName("gouthi");
//        examSample.setCriteria("3");
//        examSample.setExamID(2);
//        examSample.setLOBCriteria("e");
//
//        Mockito.when(examSamplesService.updateExamSample(Mockito.any())).thenReturn(ResponseEntity.ok("update"));
//        ResponseEntity response=examSampleController.updateById("1",examSample);
//        Assert.assertEquals(200,response.getStatusCode().value());
//    }
//
//}
//
//
