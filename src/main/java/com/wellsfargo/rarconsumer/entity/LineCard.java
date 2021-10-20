package com.wellsfargo.rarconsumer.entity;

import java.util.Date;

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
@Document(collection="ConsumerLinecard")
public class LineCard {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("line_card_id")
	private String linecardID;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@JsonProperty("examiner_id")
	private long examinerID;
	
	@JsonProperty("conclusion")
	private String conclusion;
	
	@JsonProperty("final_conclusion")
	private String finalConclusion;
	
	@JsonProperty("completed")
	private boolean completed;
	
	@JsonProperty("example_sample_ID")
	private long examSampleID;
	
	private Date assignmentDate;
	
	private String EICComments;
	private String summary;
}
