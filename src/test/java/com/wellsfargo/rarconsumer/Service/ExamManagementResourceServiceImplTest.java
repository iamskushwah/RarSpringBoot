//package com.wellsfargo.rarconsumer.Service;
//
//import com.wellsfargo.rarconsumer.entity.*;
//import com.wellsfargo.rarconsumer.repository.*;
//import com.wellsfargo.rarconsumer.response.*;
//import com.wellsfargo.rarconsumer.service.ExamManagementResourceService;
//import com.wellsfargo.rarconsumer.service.ExamManagementResourceServiceImpl;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@WebMvcTest
//@ExtendWith(MockitoExtension.class)
//public class ExamManagementResourceServiceImplTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    private ExamManagementResourceService examManagementResourceService;
//
//    @Mock
//    private UserExamRepo userExamRepo;
//
//    @Mock
//    private ExamSamplesRepo examSamplesRepo;
//
//    @Mock
//    private ExaminerInformationRepo examinerInformationRepo;
//
//    @Mock
//    private UserExamSampleRepo userExamSampleRepo;
//    private static ObjectMapper mapper = new ObjectMapper();
//
//    @Mock
//    private SystemLookupRepo systemLookupRepo;
//    private LookupValue lookupValue;
//
//    @Before
//    public void setup() {
//        userExamRepo = Mockito.mock(UserExamRepo.class);
//        userExamSampleRepo = Mockito.mock(UserExamSampleRepo.class);
//        examSamplesRepo = Mockito.mock(ExamSamplesRepo.class);
//        examinerInformationRepo = Mockito.mock(ExaminerInformationRepo.class);
//        systemLookupRepo = Mockito.mock(SystemLookupRepo.class);
//        examManagementResourceService = new ExamManagementResourceServiceImpl();
//        ReflectionTestUtils.setField(examManagementResourceService, "userExamRepo", userExamRepo);
//        ReflectionTestUtils.setField(examManagementResourceService, "userExamSampleRepo", userExamSampleRepo);
//        ReflectionTestUtils.setField(examManagementResourceService, "examSamplesRepo", examSamplesRepo);
//        ReflectionTestUtils.setField(examManagementResourceService, "examinerInformationRepo", examinerInformationRepo);
//        ReflectionTestUtils.setField(examManagementResourceService, "systemLookupRepo", systemLookupRepo);
//
//    }
//
//    @Test
//    public void test_deleteExamSample() {
//
//        Mockito.doNothing().when(userExamRepo).deleteById(Mockito.anyString());
//
//        JSONStatus examSampleResponse =  examManagementResourceService.deleteExamManagementResource(1);
//
//        Assert.assertTrue(examManagementResourceService != null);
//        Assert.assertEquals("deleted", examSampleResponse.getStatus());
//    }
//
//    @Test
//    public void test_updateExamManagementResource() {
//
//        Mockito.when(userExamRepo.save(Mockito.any(UserExam.class))).thenReturn(Mockito.mock(UserExam.class));
//        Mockito.when(userExamSampleRepo.save(Mockito.any(UserExamSample.class))).thenReturn(Mockito.mock(UserExamSample.class));
//        Mockito.when(userExamRepo.findByUserExamID(Mockito.anyLong())).thenReturn(Mockito.mock(UserExam.class));
//
//        JSONStatus Response =  examManagementResourceService.updateExamManagementResource(createUpdateRequest());
//
//        Assert.assertTrue(Response != null);
//        Assert.assertEquals("updated", Response.getStatus());
//    }
//
//    @Test
//    public void test_getAll() {
//
//        Mockito.when(examSamplesRepo.findAll()).thenReturn(createExamSamples());
//
//        ExamManagementResourceSamplesResponseDTO examSampleResponse = examManagementResourceService.getAllExamResourceSamples();
//
//        Assert.assertTrue(examSampleResponse != null);
//        Assert.assertTrue(examSampleResponse.getResult() != null && !examSampleResponse.getResult().isEmpty());
//
//        ExamManagementResourceSamplesDTO examSampleResponseDTO =  examSampleResponse.getResult().get(0);
//
//        Assert.assertTrue(examSampleResponseDTO != null);
//        Assert.assertEquals("henry", examSampleResponseDTO.getSamples());
//        Assert.assertEquals(2, examSampleResponseDTO.getSortOrder());
//        Assert.assertEquals("hen", examSampleResponseDTO.getSubName());
//
//    }
//
//    @Test
//    public void test_getAll_names() {
//
//        Mockito.when(examinerInformationRepo.findAll()).thenReturn(generateMockExaminerInformation());
//
//        ExamManagementResourceNameResponseDTO examNameResponse =  examManagementResourceService.getAllExamResourceNames();
//
//        Assert.assertTrue(examNameResponse != null);
//        Assert.assertTrue(examNameResponse.getResult() != null && !examNameResponse.getResult().isEmpty());
//
//        ExamManagementResourceNameDTO examNameResponseDTO =  examNameResponse.getResult().get(0);
//
//        Assert.assertTrue(examNameResponseDTO != null);
//        Assert.assertEquals("rob jenny", examNameResponseDTO.getUserName());
//        Assert.assertEquals(1, examNameResponseDTO.getExaminerID());
//    }
//
//    @Test
//    public void test_getAll_roles() {
//        long lookupTypeID = 4;
//        SystemLookup systemLookup = new SystemLookup();
//        List<LookupValue> rolesList = new ArrayList<>();
//        LookupValue lookupValue = new LookupValue();
//        lookupValue.setValue1("TEST");
//        lookupValue.setLookupID(1);
//        rolesList.add(lookupValue);
//        systemLookup.setLookupValue(rolesList);
//
//
//        Mockito.when(systemLookupRepo.findByLookupTypeID(lookupTypeID)).thenReturn(systemLookup);
//        ExamManagementResourceRoleResponseDTO examRoleResponse =  examManagementResourceService.getAllExamResourceRoles();
//
//        Assert.assertTrue(examRoleResponse != null);
//        Assert.assertTrue(examRoleResponse.getResult() != null && !examRoleResponse.getResult().isEmpty());
//
//        ExamManagementResourceRoleDTO examRoleResponseDTO =  examRoleResponse.getResult().get(0);
//
//        Assert.assertTrue(examRoleResponseDTO != null);
//        Assert.assertEquals("TEST", examRoleResponseDTO.getRole());
//        Assert.assertEquals(1, examRoleResponseDTO.getId());
//    }
//
//    private List<LookupValue> generateMockExaminerRoles() {
//        List<LookupValue> lookupValueList = new ArrayList<LookupValue>();
//        LookupValue lookupValue = new LookupValue();
//        lookupValue.setValue1("tom");
//        lookupValue.setLookupID(32);
//        lookupValueList.add(lookupValue);
//        return lookupValueList;
//    }
//
//    private List<ExaminerInformation> generateMockExaminerInformation() {
//        List<ExaminerInformation> examinerInformationList = new ArrayList<ExaminerInformation>();
//        ExaminerInformation examinerInformation = new ExaminerInformation();
//        examinerInformation.setFirstName("rob");
//        examinerInformation.setLastName("jenny");
//        examinerInformation.setExaminerID(1);
//        examinerInformationList.add(examinerInformation);
//        return examinerInformationList;
//    }
//
//    private ExamManagementResourceUpdateRequestDTO createUpdateRequest(){
//        ArrayList<Long> examSampleIds = new ArrayList<Long>();
//        examSampleIds.add(new Long(1));
//        examSampleIds.add(new Long(2));
//        examSampleIds.add(new Long(3));
//
//        ExamManagementResourceUpdateRequestDTO examManagementResourceUpdateRequestDTO = ExamManagementResourceUpdateRequestDTO.builder().build();
//        examManagementResourceUpdateRequestDTO.setExaminerID(1);
//        examManagementResourceUpdateRequestDTO.setExamID(2);
//        examManagementResourceUpdateRequestDTO.setResourceID(4);
//        examManagementResourceUpdateRequestDTO.setUserExamID(5);
//        examManagementResourceUpdateRequestDTO.setExamSampleID(examSampleIds);
//        return examManagementResourceUpdateRequestDTO;
//    }
//    private List<ExamSamples> createExamSamples() {
//
//        List<ExamSamples> examSamplesList = new ArrayList<ExamSamples>();
//        ExamSamples examSamples = new ExamSamples();
//        examSamples.setSampleID(1);
//        examSamples.setSampleName("henry");
//        examSamples.setSampleSubName("hen");
//        examSamples.setSortOrder(2);
//        examSamplesList.add(examSamples);
//        return examSamplesList;
//    }
//
//}
