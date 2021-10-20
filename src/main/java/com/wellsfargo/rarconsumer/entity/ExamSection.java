package com.wellsfargo.rarconsumer.entity;

import java.util.List;

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
@Document(collection="ConsumerExam")
public class ExamSection {

	@JsonProperty("section_id")
	private long sectionID;
	
	@JsonProperty("section_Name")
	private String sectionName;
	
}
