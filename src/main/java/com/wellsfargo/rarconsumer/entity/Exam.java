package com.wellsfargo.rarconsumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ConsumerExam")
public class Exam {

	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("exam_id")
	private Long examID;

	@JsonProperty("exam_name")
	private String examName;

	@JsonProperty("exam_type")
	private String examType;

	@JsonProperty("exam_type_id")
	private Long examTypeID;

	@Field("projID")
	@JsonProperty("project_id")
	private Long projectID;

	@JsonProperty("archived")
	private boolean archived;

	@JsonProperty("exam_type_abbrev")
	private String examTypeAbbrev;

	@JsonProperty("active")
	private boolean active;

	@JsonProperty("deleted")
	private boolean deleted;

	@JsonProperty("active_comment")
	private String activeComment;

	@JsonProperty("linecard_dir")
	private String linecardDir;

	@JsonProperty("linecard_dir_final")
	private String linecardDirFinal;

	@JsonProperty("delete_comment")
	private String deleteComment;

	@JsonProperty("instructions")
	private String instructions;

	@JsonProperty("linecard_staging_dir")
	private String linecardStagingDir;

	@JsonProperty("eic_name")
	private String eicName;

	@JsonProperty("data_loaded")
	private boolean dataLoaded;

	@JsonProperty("fields")
	private List<ExamFields> fields;

	@JsonProperty("linecard_sections")
	private List<ExamLinecardSections> linecardSections;

	public boolean isArchived() {
		return archived;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isDeleted() {
		return deleted;
	}

}
