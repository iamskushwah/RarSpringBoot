package com.wellsfargo.rarconsumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerUserExamSample")
public class UserExamSample {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("user_exam_sample_id")
    private long userExamSampleID;

    @JsonProperty("user_exam_id")
    private long userExamID;

    @JsonProperty("exam_sample_id")
    private long examSampleID;

    @JsonProperty("active")
    private boolean active;

}
