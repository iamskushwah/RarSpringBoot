package com.wellsfargo.rarconsumer.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellsfargo.rarconsumer.entity.ProjectMaster;

public interface ProjectMasterRepo extends MongoRepository<ProjectMaster, Long>{

	Optional<ProjectMaster> findByProjectID(long projectId);
	
	Optional<List<ProjectMaster>> findByEicIDIn(List<Long> eicIds);
	
	List<ProjectMaster> findByExamDataTypeIgnoreCaseAndProjectStatusNotIgnoreCaseAndProjectLUNameIsNotNullAndYearExaminedIn(String dataType,String status,Collection<Integer> years);

}
