//package com.wellsfargo.rarconsumer.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
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
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wellsfargo.rarconsumer.entity.ExamSamples;
//import com.wellsfargo.rarconsumer.repository.ExamSamplesRepo;
//import com.wellsfargo.rarconsumer.response.ExamSamplesResponseDTO;
//import com.wellsfargo.rarconsumer.response.ExamSamplesResponsePageableDTO;
//import com.wellsfargo.rarconsumer.service.ExamSamplesService;
//import com.wellsfargo.rarconsumer.service.ExamSamplesServiceImpl;
//
//@WebMvcTest
//@ExtendWith(MockitoExtension.class)
//public class ExamSamplesServiceImplTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    private ExamSamplesService examSamplesService;
//
//    @Mock
//    private ExamSamplesRepo examSamplesRepo;
//    private static ObjectMapper mapper = new ObjectMapper();
//
//    @Before
//    public void setup() {
//        examSamplesRepo = Mockito.mock(ExamSamplesRepo.class);
//        examSamplesService = new ExamSamplesServiceImpl();
//        ReflectionTestUtils.setField(examSamplesService, "examSamplesRepo", examSamplesRepo);
//    }
//
//    @Test
//    public void test_getAll() {
//
//        Mockito.when(examSamplesRepo.findAll()).thenReturn(generateMockExamSamples());
//
//        ExamSamplesResponsePageableDTO examSampleResponse =  examSamplesService.getAll();
//
//        Assert.assertTrue(examSampleResponse != null);
//        Assert.assertTrue(examSampleResponse.getResult() != null && !examSampleResponse.getResult().isEmpty());
//
//        ExamSamplesResponseDTO examSampleResponseDTO =  examSampleResponse.getResult().get(0);
//
//        Assert.assertTrue(examSampleResponseDTO != null);
//        Assert.assertEquals("1", examSampleResponseDTO.getId());
//        Assert.assertEquals("goutham", examSampleResponseDTO.getSampleName());
//        Assert.assertEquals("3", examSampleResponseDTO.getCriteria());
//        Assert.assertEquals(2, examSampleResponseDTO.getExamID());
//        Assert.assertEquals("e", examSampleResponseDTO.getLOBCriteria());
//
//    }
//
//    private List<ExamSamples> generateMockExamSamples() {
//
//        List<ExamSamples> examSampleList = new ArrayList<ExamSamples>();
//        ExamSamples examSamples = new ExamSamples();
//        examSamples.setId("1");
//        examSamples.setActive(true);
//        examSamples.setSampleName("goutham");
//        examSamples.setCriteria("3");
//        examSamples.setExamID(2);
//        examSamples.setLOBCriteria("e");
//        examSampleList.add(examSamples);
//        return examSampleList;
//    }
//
//    @Test
//    public void test_getExamSample() {
//
//        Mockito.when(examSamplesRepo.findAll(Mockito.any(Pageable.class))).thenReturn(null);
//        examSamplesService.getExamSamples(0, 1);
//
//    }
//
//    @Test
//    public void test_deleteExamSample() {
//
//        Mockito.doNothing().when(examSamplesRepo).deleteById(Mockito.anyString());
//
//        ResponseEntity examSampleResponse =  examSamplesService.deleteExamSample("1");
//
//        Assert.assertTrue(examSampleResponse != null);
//        Assert.assertEquals(HttpStatus.OK, examSampleResponse.getStatusCode());
//        Assert.assertEquals("deleted", examSampleResponse.getBody());
//
//    }
//
//    @Test
//    public void test_loadExamSampleById() {
//
//        Mockito.when(examSamplesRepo.findById("1")).thenReturn(generateMockOptionalExamSamples());
//
//        ExamSamplesResponseDTO examSampleResponse =  examSamplesService.loadExamSampleById("1");
//
//        Assert.assertTrue(examSampleResponse != null);
//        Assert.assertEquals("1", examSampleResponse.getId());
//        Assert.assertEquals("goutham", examSampleResponse.getSampleName());
//        Assert.assertEquals("3", examSampleResponse.getCriteria());
//        Assert.assertEquals(2, examSampleResponse.getExamID());
//        Assert.assertEquals("e", examSampleResponse.getLOBCriteria());
//
//    }
//
//    private Optional<ExamSamples> generateMockOptionalExamSamples() {
//        ExamSamples examSamples = new ExamSamples();
//        examSamples.setId("1");
//        examSamples.setSampleName("goutham");
//        examSamples.setCriteria("3");
//        examSamples.setExamID(2);
//        examSamples.setLOBCriteria("e");
//        return Optional.of(examSamples);
//    }
//
//    @Test
//    public void test_updateExamSample() {
//
//        ExamSamples examSample = new ExamSamples();
//        examSample.setSampleName("goutham");
//        examSample.setCriteria("3");
//        examSample.setExamID(2);
//        examSample.setLOBCriteria("e");
//        examSample.setSampleSubName("gouth");
//        examSample.setSortOrder(1);
//
//        Mockito.when(examSamplesRepo.save(Mockito.any(ExamSamples.class))).thenReturn(examSample);
//
//        ResponseEntity Response =  examSamplesService.updateExamSample(examSample);
//
//        Assert.assertTrue(Response != null);
//        Assert.assertEquals(HttpStatus.OK, Response.getStatusCode());
//        Assert.assertEquals("updated", Response.getBody());
//
//    }
//
//    @Test
//    public void test_updateExamSample_error() {
//
//        ExamSamples examSample = new ExamSamples();
//        examSample.setId("1");
//        examSample.getSampleName();
//        examSample.setCriteria("3");
//        examSample.setExamID(2);
//        examSample.setLOBCriteria("e");
//
//        Mockito.when(examSamplesRepo.save(Mockito.any(ExamSamples.class))).thenReturn(examSample);
//
//        ResponseEntity Response =  examSamplesService.updateExamSample(examSample);
//
//        Assert.assertTrue(Response != null);
//        Assert.assertEquals(HttpStatus.BAD_REQUEST, Response.getStatusCode());
//        Assert.assertEquals("Validation failure", Response.getBody());
//
//    }
//
//}
//
