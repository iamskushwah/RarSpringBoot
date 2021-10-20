package com.wellsfargo.rarconsumer.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
@Document(collection="ConsumerGuestExaminer")
public class ConsumerGuestExaminer {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("examiner_id")
	private long examinerID;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonProperty("lan_id")
	private String lanID;
	
	@JsonProperty("manager_id")
	private long managerID;
	
	@JsonProperty("role")
	private String role;
	
	@JsonProperty("start_date")
	private Date startDate;
	
	@JsonProperty("end_date")
	private Date endDate;
	
	@JsonProperty("active")
	private boolean active;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("updated_date")
	private Date updatedDate;
	
	@JsonProperty("updated_by")
	private String updatedBy;
	
	
}
