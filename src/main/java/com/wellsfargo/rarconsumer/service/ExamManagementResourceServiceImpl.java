package com.wellsfargo.rarconsumer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.wellsfargo.rarconsumer.entity.ExamSamples;
import com.wellsfargo.rarconsumer.entity.ExaminerInformation;
import com.wellsfargo.rarconsumer.entity.ExaminerSettings;
import com.wellsfargo.rarconsumer.entity.LookupValue;
import com.wellsfargo.rarconsumer.entity.SystemLookup;
import com.wellsfargo.rarconsumer.entity.UserExam;
import com.wellsfargo.rarconsumer.entity.UserExamSample;
import com.wellsfargo.rarconsumer.repository.ExamSamplesRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerInformationRepo;
import com.wellsfargo.rarconsumer.repository.ExaminerSettingsRepo;
import com.wellsfargo.rarconsumer.repository.SystemLookupRepo;
import com.wellsfargo.rarconsumer.repository.UserExamRepo;
import com.wellsfargo.rarconsumer.repository.UserExamSampleRepo;
import com.wellsfargo.rarconsumer.request.ExamManagementResourceRequestDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceNameDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceRoleDTO;
import com.wellsfargo.rarconsumer.response.ExamManagementResourceSamplesDTO;
import com.wellsfargo.rarconsumer.response.JSONStatus;
import com.wellsfargo.rarconsumer.response.MaxIDDTO;
import com.wellsfargo.rarconsumer.response.ResourceSummaryGridDTO;
import com.wellsfargo.rarconsumer.util.AggregationUtils;

@Service
public class ExamManagementResourceServiceImpl implements ExamManagementResourceService {

    @Autowired
    private UserExamRepo userExamRepo;

    @Autowired
    private ExamSamplesRepo examSamplesRepo;

    @Autowired
    private UserExamSampleRepo userExamSampleRepo;

    @Autowired
    private ExaminerInformationRepo examinerInformationRepo;

    @Autowired
    private SystemLookupRepo systemLookupRepo;

    @Autowired
    ExaminerSettingsRepo examinerSettingsRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

    @Override
    public List<ExamManagementResourceDTO> getExamManagementResource(long examinerID) {
        long lookupTypeID = 4;
        Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerID);
        if (examinerSettings.isPresent()) {
            long lastExamID = examinerSettings.get().getLastExamID();
            SystemLookup roleLookup = systemLookupRepo.findByLookupTypeID(lookupTypeID);
            List<LookupValue> rolesList = roleLookup.getLookupValue();
            HashMap<Long,String> rolesLookup = new HashMap();
            rolesList.forEach(typeValue -> {
            	rolesLookup.put(typeValue.getLookupID(),typeValue.getValue1());
            });
            String whereoperation = AggregationUtils.getMatchOperation("examID",lastExamID);
            String userexamsampleoperation = AggregationUtils.getLookupAggregation("ConsumerUserExamSample", "userExamID", "userExamID", "usersample");  		
            String examsampleoperation = AggregationUtils.getLookupAggregation("ConsumerExamSample", "usersample.examSampleID", "sampleID", "sampledata");
            String examineroperation = AggregationUtils.getLookupAggregation("ExaminerInformation", "examinerID", "examinerID", "examinerdata");
            String unwindoperation = AggregationUtils.getUnwindAggregation("examinerdata");
            String projectoperation = "{$project:{'userExamID':'$userExamID','firstName':'$examinerdata.firstName','lastName':'$examinerdata.lastName','samples':'$sampledata.sampleName','examID':'$examID','role':'$resourceID'}}";
			Aggregation aggregation = AggregationUtils.prepareAggregation(whereoperation,userexamsampleoperation,examsampleoperation,examineroperation,unwindoperation,projectoperation);
			AggregationResults<ResourceSummaryGridDTO> aggregationResult = mongoTemplate.aggregate(aggregation,"ConsumerUserExam", ResourceSummaryGridDTO.class);
			List<ResourceSummaryGridDTO> mappedResult = aggregationResult.getMappedResults();
			List<ExamManagementResourceDTO> gridResult = new ArrayList();
			mappedResult.stream().forEach(currResource-> {
				ExamManagementResourceDTO currDTO = new ExamManagementResourceDTO();
				currDTO.setExamId(lastExamID);
				currDTO.setUserName(currResource.getFirstName()+" "+currResource.getLastName());
				currDTO.setSamples(currResource.getSamples());
				currDTO.setRole(rolesLookup.get(currResource.getRole()));
				currDTO.setUserExamID(currResource.getUserExamID());
				gridResult.add(currDTO);
			});
			return gridResult;
        }
        return null;
    }

    @Override
    public JSONStatus createExamManagementResourceService(ExamManagementResourceRequestDTO requestDTO,long examinerid) {
        List<UserExam> userExamList = userExamRepo.findAll();
        String maxoperation = AggregationUtils.getMaxAggregation("userExamID");
		Aggregation aggregation = AggregationUtils.prepareAggregation(maxoperation);
		AggregationResults<MaxIDDTO> aggregationResult = mongoTemplate.aggregate(aggregation,"ConsumerUserExam", MaxIDDTO.class);
		long uniqueID = aggregationResult.getMappedResults().get(0).getMaxID()+1;
        Optional<ExaminerSettings> examinerSettings = examinerSettingsRepo.findByExaminerID(examinerid);
        UserExam userExam = new UserExam();
        if (examinerSettings.isPresent()) {
            long lastExamId = examinerSettings.get().getLastExamID();
            userExam.setExamID(lastExamId);
            userExam.setUserExamID(uniqueID);
            userExam.setResourceID(requestDTO.getResourceID());
            userExam.setExaminerID(requestDTO.getExaminerID());
            userExamRepo.save(userExam);
        }
        List<Long> examSampleIds = requestDTO.getExamSampleID();
        if (examSampleIds != null)
            examSampleIds.forEach(examSampleID -> {
                UserExamSample userExamSample = new UserExamSample();
                userExamSample.setExamSampleID(examSampleID);
                userExamSample.setUserExamID(uniqueID);
                userExamSampleRepo.save(userExamSample);
            });
        JSONStatus status = new JSONStatus();
        status.setStatus("created");
        return status;
    }

    @Override
    public JSONStatus deleteExamManagementResource(long userExamID) {
        JSONStatus status = new JSONStatus();
        status.setStatus("deleted");
        userExamSampleRepo.deleteAllByUserExamID(userExamID);
        userExamRepo.deleteAllByUserExamID(userExamID);
        return status;
    }

    @Override
    public JSONStatus updateExamManagementResource(ExamManagementResourceRequestDTO requestDTO) {
        JSONStatus status = new JSONStatus();
        List<Long> examSampleIds = requestDTO.getExamSampleID();
        userExamSampleRepo.deleteAllByUserExamID(requestDTO.getUserExamID());

        examSampleIds.forEach(examSampleID -> {
            UserExamSample userExamSample = new UserExamSample();
            userExamSample.setExamSampleID(examSampleID);
            userExamSample.setUserExamID(requestDTO.getUserExamID());
            userExamSampleRepo.save(userExamSample);
        });

        UserExam userExam = userExamRepo.findByUserExamID(requestDTO.getUserExamID());
        userExam.setExaminerID(requestDTO.getExaminerID());
        userExam.setResourceID(requestDTO.getResourceID());
        userExam.setExamID(requestDTO.getExamID());
        userExamRepo.save(userExam);
        status.setStatus("updated");
        return status;
    }

    @Override
    public List<ExamManagementResourceNameDTO> getAllExamResourceNames() {
        List<ExaminerInformation> examinerInformation = examinerInformationRepo.findAll();
        List<ExamManagementResourceNameDTO> result = new ArrayList<ExamManagementResourceNameDTO>();
        examinerInformation.forEach(examInfo -> {
            ExamManagementResourceNameDTO dtoObj = new ExamManagementResourceNameDTO();
            dtoObj.setExaminerID(examInfo.getExaminerID());
            dtoObj.setUserName(examInfo.getFirstName() + " " + examInfo.getLastName());
            result.add(dtoObj);
        });
        return result;
    }

    @Override
    public List<ExamManagementResourceRoleDTO> getAllExamResourceRoles() {
        long lookupTypeID = 4;
        SystemLookup roleLookup = systemLookupRepo.findByLookupTypeID(lookupTypeID);
        List<LookupValue> rolesList = roleLookup.getLookupValue();
        List<ExamManagementResourceRoleDTO> result = new ArrayList<ExamManagementResourceRoleDTO>();
        rolesList.forEach(typeValue -> {
            ExamManagementResourceRoleDTO resourceRoleDTO = new ExamManagementResourceRoleDTO();
            resourceRoleDTO.setId(typeValue.getLookupID());
            resourceRoleDTO.setRole(typeValue.getValue1());
            result.add(resourceRoleDTO);
        });
        return result;
    }

    @Override
    public List<ExamManagementResourceSamplesDTO> getAllExamResourceSamples() {

        List<ExamSamples> examSamples = examSamplesRepo.findAll();
        List<ExamManagementResourceSamplesDTO> resourceSamplesDTOS = new ArrayList<ExamManagementResourceSamplesDTO>();

        examSamples.forEach(samplesInfo -> {
            ExamManagementResourceSamplesDTO dtoObj = new ExamManagementResourceSamplesDTO();
            dtoObj.setExamSampleID(samplesInfo.getSampleID());
            dtoObj.setSamples(samplesInfo.getSampleName());
            dtoObj.setSubName(samplesInfo.getSampleSubName());
            dtoObj.setSortOrder(samplesInfo.getSortOrder());
            resourceSamplesDTOS.add(dtoObj);
        });

        return resourceSamplesDTOS;
    }
}
