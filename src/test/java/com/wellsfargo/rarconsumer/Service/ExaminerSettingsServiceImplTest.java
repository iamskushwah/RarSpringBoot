package com.wellsfargo.rarconsumer.Service;

import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.service.ExaminerSettingsService;
import com.wellsfargo.rarconsumer.service.ExaminerSettingsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;


@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class ExaminerSettingsServiceImplTest {

    @Autowired
    private MockMvc mockMvc;
    private ExaminerSettingsService examinerSettingsService;

    @Mock
    private ExaminerSettingsRepo examinerSettingsRepo;
    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        examinerSettingsRepo = Mockito.mock(ExaminerSettingsRepo.class);
        examinerSettingsService = new ExaminerSettingsServiceImpl();
        ReflectionTestUtils.setField(examinerSettingsService, "examinerSettingsRepo", examinerSettingsRepo);
    }

    @Test
    public void test_updateExaminerSettings() {

        ExaminerSettings examinerSetting = new ExaminerSettings();

        examinerSetting.setExaminerID(123);
        examinerSetting.setLastExamID(34);

        Mockito.when(examinerSettingsRepo.save(Mockito.any(ExaminerSettings.class))).thenReturn(examinerSetting);

        ResponseEntity Response =  examinerSettingsService.updateLastExamInExaminerSettings(123,34);

        Assert.assertTrue(Response != null);
        Assert.assertEquals(HttpStatus.OK, Response.getStatusCode());
        JSONStatus status = (JSONStatus) Response.getBody();
        Assert.assertEquals("updated", status.getStatus());

    }

    private Optional<ExaminerSettings> generateMockOptionalExaminerSettings() {
        ExaminerSettings examinerSetting = new ExaminerSettings();
        examinerSetting.setExaminerID(123);
        examinerSetting.setLastExamID(34);
        return Optional.of(examinerSetting);
    }

    @Test
    public void test_updateExaminerSettings_Error() {

        ExaminerSettings examinerSetting = new ExaminerSettings();
        examinerSetting.setExaminerID(123);
        examinerSetting.setLastExamID(34);

        Mockito.when(examinerSettingsRepo.save(Mockito.any(ExaminerSettings.class))).thenReturn(examinerSetting);

        ResponseEntity Response =  examinerSettingsService.updateLastExamInExaminerSettings(123,34);

        Assert.assertTrue(Response != null);
        Assert.assertEquals(HttpStatus.OK, Response.getStatusCode());
        JSONStatus status = (JSONStatus) Response.getBody();
        Assert.assertEquals("updated", status.getStatus());

    }
}
