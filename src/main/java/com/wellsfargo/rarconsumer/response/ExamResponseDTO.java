package com.wellsfargo.rarconsumer.response;
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
    private Long projectId;

    @JsonProperty("archived")
    private Boolean archived;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("eic_name")
    private String eicName;
    
    @JsonProperty("delete")
    private Boolean delete;
}
