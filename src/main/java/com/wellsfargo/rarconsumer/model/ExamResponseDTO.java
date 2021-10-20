package com.wellsfargo.rarconsumer.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("exam_id")
    private long examId;

    @JsonProperty("exam_name")
    private String examName;

    @JsonProperty("exam_type")
    private String examType;

    @JsonProperty("project_id")
    private long projectId;

	/*@JsonProperty("eic_name")
	private String eicName;*/

	/*@JsonProperty("exam_creation_type")
	private String examCreationType;*/

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("eic_name")
    private String eicName;
    
    @JsonProperty("delete")
    private boolean delete;
    
    @JsonProperty("data_loaded")
    private boolean dataLoaded;
    
    @JsonProperty("status")
    private String status;
}

