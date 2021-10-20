package com.wellsfargo.rarconsumer.entity;

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
@Document(collection="ConsumerLinecard")
public class ConsumerLinecard {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("linecard_id")
	private String linecardID;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@JsonProperty("exam_sample_id")
	private long examSampleID;
	
	@JsonProperty("examiner_id")
	private long examinerID;
	
	@JsonProperty("conclusion")
	private String conclusion;
	
	@JsonProperty("completed")
	private boolean completed;
	
	@JsonProperty("summary")
	private String summary;
	
	@JsonProperty("eic_comments")
	private String eicComments;
	
	@JsonProperty("final_conclusion")
	private String finalConclusion;
	
	@JsonProperty("source_data_file")
	private String sourceDataFile;
	
	@JsonProperty("assignment_date")
	private Date assignmentDate;
	
	@JsonProperty("completion_date")
	private Date completionDate;
	
	@JsonProperty("updated_date")
	private Date updatedDate;
	
	@JsonProperty("updated_by")
	private String updatedBy;
	
	@JsonProperty("delete_comment")
	private String deleteComment;
	
	
	
}
