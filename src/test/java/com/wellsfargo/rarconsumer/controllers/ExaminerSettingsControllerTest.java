package com.wellsfargo.rarconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.response.ResponseApiDTO;
import com.wellsfargo.rarconsumer.service.ExaminerSettingsService;
import com.wellsfargo.rarconsumer.service.ExaminerSettingsServiceImpl;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class ExaminerSettingsControllerTest {


    private ExaminerSettingsController examinerSettingsController;

    @Mock
    private ExaminerSettingsService examinerSettingsService;

    private static ObjectMapper mapper = new ObjectMapper();

   @Before
    public void setup() throws Exception {
        examinerSettingsService = Mockito.mock(ExaminerSettingsServiceImpl.class);
        examinerSettingsController = new ExaminerSettingsController();
        ReflectionTestUtils.setField(examinerSettingsController, "examinerSettingsService", examinerSettingsService);

    }

   @Test
    public void ExaminerTest() throws Exception{
       ExaminerSettings examinerSetting = new ExaminerSettings();
       examinerSetting.setExaminerID(123);
       examinerSetting.setLastExamID(34);

       ResponseApiDTO dto=new ResponseApiDTO();

       ResponseEntity<ResponseApiDTO> responseExpected=new ResponseEntity<ResponseApiDTO>(HttpStatus.OK);

       Mockito.when(examinerSettingsService.updateLastExamInExaminerSettings(Mockito.anyLong(),Mockito.anyLong())).thenReturn(responseExpected);
       ResponseEntity<ResponseApiDTO> response= examinerSettingsController.updateExaminerSettings(123, 34);
       Assert.assertEquals(200,response.getStatusCode().value());
   }

}
