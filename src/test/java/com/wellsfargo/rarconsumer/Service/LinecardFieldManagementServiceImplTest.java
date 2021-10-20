package com.wellsfargo.rarconsumer.Service;

import com.wellsfargo.rarconsumer.entity.*;
import com.wellsfargo.rarconsumer.repository.*;
import com.wellsfargo.rarconsumer.response.*;
import com.wellsfargo.rarconsumer.service.ExamLinecardService;
import com.wellsfargo.rarconsumer.service.LinecardFieldManagementService;
import com.wellsfargo.rarconsumer.service.LinecardFieldManagementServiceImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class LinecardFieldManagementServiceImplTest {

    @Autowired
    private MockMvc mockMvc;
    private LinecardFieldManagementService linecardFieldManagementService;
    private Exam exam;

    @Mock
    private ExaminerSettingsRepo examinerSettingsRepo;

    @Mock
    private ExamRepo examRepo;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ExamLinecardService linecardService;

    @Mock
    private LinecardFieldManagementRepo linecardFieldManagementRepo;
    private static ObjectMapper mapper = new ObjectMapper();

    @Mock
    AggregationResults aggregationResults;

    @Before
    public void setup() {
        examinerSettingsRepo = Mockito.mock(ExaminerSettingsRepo.class);
        examRepo = Mockito.mock(ExamRepo.class);
        linecardService = Mockito.mock(ExamLinecardService.class);
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        aggregationResults = Mockito.mock(AggregationResults.class);

        linecardFieldManagementRepo = Mockito.mock(LinecardFieldManagementRepo.class);
        linecardFieldManagementService = new LinecardFieldManagementServiceImpl();
        ReflectionTestUtils.setField(linecardFieldManagementService, "examinerSettingsRepo", examinerSettingsRepo);
        ReflectionTestUtils.setField(linecardFieldManagementService, "examRepo", examRepo);
        ReflectionTestUtils.setField(linecardFieldManagementService, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(linecardFieldManagementService, "linecardFieldManagementRepo", linecardFieldManagementRepo);
        ReflectionTestUtils.setField(linecardFieldManagementService, "linecardService", linecardService);
    }

    @Test
    public void test_deleteLinecardField() {
        ExaminerSettings examinerSettings = new ExaminerSettings();
        Exam currExam = new Exam();
        List<ExamFields> fields = new ArrayList<>();
        ExamFields examFields = new ExamFields();
        examFields.setFieldID((long) 3);
        fields.add(examFields);
        currExam.setFields(fields);
        Mockito.when(examinerSettingsRepo.findByExaminerID(Matchers.anyLong())).thenReturn(Optional.of(examinerSettings));
        Mockito.when(examRepo.findFirstByExamID(Matchers.anyLong())).thenReturn(currExam);

        JSONStatus linecardFieldsResponse = linecardFieldManagementService.deleteLinecardField(new Long(1),new Long(2));

        Assert.assertTrue(linecardFieldManagementService != null);
        Assert.assertEquals("deleted", linecardFieldsResponse.getStatus());
    }

    @Test
    public void test_updateLinecardField() throws Exception {
        Fields fields = new Fields();
        fields.setFieldID(123);
        ExamFields examFields = new ExamFields();

        Mockito.when(linecardFieldManagementRepo.save(Mockito.any(Fields.class))).thenReturn(Mockito.mock(Fields.class));
        JSONStatus Response = linecardFieldManagementService.updateLinecardField(LinecardFieldManagementDTO.builder().build());

        Assert.assertTrue(Response != null);
        Assert.assertEquals("updated", Response.getStatus());
    }

    @Test
    public void test_getExamFieldsBySection() throws Exception {
        ExamFields examFields = new ExamFields();
        List<LinecardFieldManagementDTO> linecardFields = new ArrayList<>();
        LinecardFieldManagementDTO linecardFields1 = new LinecardFieldManagementDTO();
        linecardFields1.setToolTip("TEST");
        linecardFields1.setCaption("Test_1");
        linecardFields1.setSection("value");
        linecardFields1.setCollName("DataUtility");
        linecardFields1.setSection("value");
        linecardFields.add(linecardFields1);
        Document rawResults = new Document();
        AggregationResults<LinecardFieldManagementDTO> aggregationResultExp = new AggregationResults(linecardFields, (new Document("results", null)).append("ok", 1.0D));
        Mockito.when(mongoTemplate.aggregate(Matchers.any(Aggregation.class), Matchers.anyString(), Matchers.eq(LinecardFieldManagementDTO.class))).thenReturn(aggregationResultExp);
        List<LinecardFieldManagementDTO> linecardsFields = linecardFieldManagementService.getExamFieldsBySection(new Long(1), "value");
        Assert.assertTrue(linecardsFields != null);
        LinecardFieldManagementDTO linecardResponseDTO = linecardsFields.get(0);

        Assert.assertTrue(linecardResponseDTO != null);
        Assert.assertEquals("Test_1", linecardResponseDTO.getCaption());
        Assert.assertEquals("TEST", linecardResponseDTO.getToolTip());
        Assert.assertEquals("value", linecardResponseDTO.getSection());
        Assert.assertEquals("DataUtility", linecardResponseDTO.getCollName());
    }

    @Test
    public void test_getAllExamFields() throws Exception {
        ExaminerSettings examinerSettings = new ExaminerSettings();
        Exam currExam = new Exam();
        List<ExamFields> fields = new ArrayList<>();
        currExam.setFields(fields);
        List<LinecardFieldManagementDTO> aggregationlist = new ArrayList<>();
        LinecardFieldManagementDTO linecardFieldManagementDTO = new LinecardFieldManagementDTO();
        linecardFieldManagementDTO.setSection("TEST");
        aggregationlist.add(linecardFieldManagementDTO);

        Mockito.when(aggregationResults.getMappedResults()).thenReturn(aggregationlist);

        Mockito.when(mongoTemplate.aggregate(Matchers.anyObject(), Matchers.anyString(), Matchers.any()))
                .thenReturn(aggregationResults);
        AggregationResults<LinecardFieldManagementDTO> aggregationResultExp = new AggregationResults((aggregationlist), (new Document("results", null)).append("ok", 1.0D));
        Mockito.when(mongoTemplate.aggregate(Matchers.any(Aggregation.class), Matchers.anyString(), Matchers.eq(LinecardFieldManagementDTO.class))).thenReturn(aggregationResultExp);
        List<LinecardFieldManagementDTO> linecardsFields = linecardFieldManagementService.getExamFieldsBySection(new Long(1), "value");
        Assert.assertTrue(linecardsFields != null);
        Assert.assertTrue(linecardFieldManagementService != null);
    }

    @Test
    public void test_createLinecardField() throws Exception {

        List<LinecardFieldManagementDTO> linecardFields = new ArrayList<>();
        LinecardFieldManagementDTO linecardFields1 = new LinecardFieldManagementDTO();

        linecardFields1.setToolTip("TEST");
        linecardFields1.setCaption("Test_1");
        linecardFields1.setSection("value");
        linecardFields1.setCollName("DataUtility");
        linecardFields1.setStatus("value1");

        Fields fields = new Fields();
        fields.setFieldID(123);

        List<ExaminerSettings> exmsettings = new ArrayList<>();
        ExaminerSettings exmsetting = new ExaminerSettings();
        exmsetting.setLastExamID(123);
        exmsettings.add(exmsetting);
        Exam examres = new Exam();
        List<ExamFields> fieldsres = new ArrayList<>();
        ExamFields examFields = new ExamFields();
        examFields.setFieldID(new Long(1));
        fieldsres.add(examFields);
        examres.setFields(fieldsres);
        Mockito.when(examinerSettingsRepo.findByExaminerID(Matchers.anyLong())).thenReturn(exmsettings.stream().findFirst());
        Mockito.when(examRepo.findFirstByExamID(Matchers.anyLong())).thenReturn(examres);
        List<MaxIDDTO> dtoLIst = new ArrayList<>();
        MaxIDDTO dto = new MaxIDDTO();
        dto.setMaxID(1);
        dtoLIst.add(dto);
        AggregationResults<MaxIDDTO> aggregationResult = new AggregationResults(dtoLIst, (new Document("results", null)).append("ok", 1.0D));
        Mockito.when(mongoTemplate.aggregate(Matchers.any(Aggregation.class), Matchers.anyString(), Matchers.eq(MaxIDDTO.class))).thenReturn(aggregationResult);
        List<LinecardSectionResponseDTO> respDtos = new ArrayList<>();
        LinecardSectionResponseDTO resp = new LinecardSectionResponseDTO();
        resp.setSection("value");
        respDtos.add(resp);
        Mockito.when(linecardService.getExaminerLCSections(Matchers.anyLong())).thenReturn(respDtos);
        Mockito.when(examRepo.save(Mockito.any(Exam.class))).thenReturn(Mockito.mock(Exam.class));

        Mockito.when(linecardFieldManagementRepo.save(Matchers.any())).thenReturn(Mockito.mock(Fields.class));
        JSONStatus response = linecardFieldManagementService.createLinecardField(linecardFields1, new Long(123));

        Assert.assertTrue(response != null);
        Assert.assertEquals("created", response.getStatus());

    }
}
