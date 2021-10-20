package com.wellsfargo.rarconsumer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceRequestDTO {

    @JsonProperty("user_exam_id")
    private long userExamID;

    @JsonProperty("examiner_id")
    private long examinerID;

    @JsonProperty("exam_id")
    private long examID;

    @JsonProperty("resource_id")
    private int resourceID;

    @JsonProperty("exam_sample_id")
    private List<Long> examSampleID;

}
