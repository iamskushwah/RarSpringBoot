package com.wellsfargo.rarconsumer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection="ProjectMaster")
public class ProjectMaster {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("project_id")
	private Long projectID;
	
	@JsonProperty("year_examined")
	private Long yearExamined;

	@Field("EICID")
	@JsonProperty("eic_id")
	private Long eicID;
	
	@JsonProperty("project_lu_name")
	private String projectLUName;
	
	@JsonProperty("exam data_type")
	private String examDataType;
	
	@JsonProperty("project_status")
	private String projectStatus;
	
}
