package com.wellsfargo.rarconsumer.entity;

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
@Document(collection = "ConsumerUserExam")
public class UserExam {

	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("user_exam_id")
	private long userExamID;

	@JsonProperty("examiner_id")
	private long examinerID;

	@JsonProperty("exam_id")
	private long examID;

	@JsonProperty("resource_id")
	private int resourceID;

	@JsonProperty("active")
	private boolean active;

	@JsonProperty("linecardSections")
	private List<ExamLinecardSections> linecardSections;

}
