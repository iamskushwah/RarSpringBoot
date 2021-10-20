package com.wellsfargo.rarconsumer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ExaminerInformation")
public class ExaminerInformation {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("examiner_id")
	private Long examinerID;
	
	@JsonProperty("employee_id")
	private long employeeId;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonProperty("nick_name")
	private String nickName;
	
	@JsonProperty("department")
	private String department;
	
	@JsonProperty("last_exam_id")
	private long lastExamID;
	
	@JsonProperty("employee_group")
	private String employeeGroup;
	
	@JsonProperty("active")
	private Boolean active;
}
