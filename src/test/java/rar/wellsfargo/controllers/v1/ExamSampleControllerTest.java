//package rar.wellsfargo.controllers.v1;
//
//
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import rar.wellsfargo.entity.ExamSamples;
//import rar.wellsfargo.model.ExamSampleRequestDTO;
//import rar.wellsfargo.model.ExamSamplesResponseDTO;
//import rar.wellsfargo.response.ExamSamplesResponsePageableDTO;
//import rar.wellsfargo.service.ExamSamplesService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@WebMvcTest
//    public class ExamSampleControllerTest {
//
//        @Autowired
//        private MockMvc mockMvc;
//
//        @InjectMocks
//        private ExamSampleController examSampleController;
//
//        @Mock
//        private ExamSamplesService examSamplesService;
//        private static ObjectMapper mapper = new ObjectMapper();
//
//        @Test
//        public void testGetExamSamples() throws Exception {
//
//            Mockito.when(examSamplesService.getAll()).thenReturn(generateMockExamSamples());
//
//            ExamSamplesResponsePageableDTO responseEntity = examSampleController.getExamSamples();
//
//            Assert.assertTrue(responseEntity != null);
//            Assert.assertTrue(responseEntity.getResult() != null && responseEntity.getResult().size() > 0);
//            ExamSamplesResponseDTO examSample = responseEntity.getResult().get(0);
//        }
//
//        private  ExamSamplesResponsePageableDTO generateMockExamSamples(){
//
//            List<ExamSamples> examSampleList = new ArrayList<ExamSamples>();
//            ExamSamples examSamples = new ExamSamples();
//            examSamples.setId("1");
//            examSamples.setActive(true);
//            examSamples.getSampleName();
//            examSamples.setCriteria("3");
//            examSamples.setExamID(2);
//            examSamples.setLOBCriteria("e");
//            examSampleList.add(examSamples);
//            List<ExamSamplesResponseDTO> examResponse = new ArrayList<ExamSamplesResponseDTO>();
//
//            for(ExamSamples element: examSampleList) {
//                examResponse.add(setExamSamplesResponse(element));
//            }
//
//            return ExamSamplesResponsePageableDTO.builder().result(examResponse).build();
//        }
//
//        private ExamSamplesResponseDTO setExamSamplesResponse(ExamSamples examSamplesData) {
//
//        ExamSamplesResponseDTO examSamplesResponse = ExamSamplesResponseDTO.builder().id(examSamplesData.getId()).
//                sampleID(examSamplesData.getSampleID()).sampleName(examSamplesData.getSampleName())
//                .sampleSubName(examSamplesData.getSampleSubName()).LOBCriteria(examSamplesData.getLOBCriteria())
//                .examID(examSamplesData.getExamID()).sortOrder(examSamplesData.getSortOrder()).criteria(examSamplesData.getCriteria()).build();
//
//        return examSamplesResponse;
//    }
//
//
//        @Test
//        public void testCreateExamSamples() throws Exception {
//
//            ExamSampleRequestDTO examSample = new ExamSampleRequestDTO();
//            examSample.setCriteria("1");
//            examSample.setActive(false);
//
//            Mockito.when(examSamplesService.saveExamSample(examSample)).thenReturn(ResponseEntity.ok("created"));
//            ResponseEntity response=examSampleController.createExamSamples(examSample);
//
//            Assert.assertEquals(200,response.getStatusCode().value());
//
//        }
//
//
//        @Test
//        public void testDeletedById() throws Exception {
//
//            Mockito.when(examSamplesService.deleteExamSample(Mockito.anyString())).thenReturn(ResponseEntity.ok("deleted"));
//            ResponseEntity response=examSampleController.deleteById("1");
//            Assert.assertEquals(200,response.getStatusCode().value());
//        }
//
//
//        @Test
//        public void testUpdateExamSample() throws Exception {
//            ExamSamples examSample = new ExamSamples();
//            examSample.setActive(true);
//            examSample.setSampleName("goutham");
//            examSample.setSampleSubName("gouthi");
//            examSample.setCriteria("3");
//            examSample.setExamID(2);
//            examSample.setLOBCriteria("e");
//
//            Mockito.when(examSamplesService.updateExamSample(Mockito.any())).thenReturn(ResponseEntity.ok("update"));
//            ResponseEntity response=examSampleController.updateById("1",examSample);
//            Assert.assertEquals(200,response.getStatusCode().value());
//        }
//
//}
//
