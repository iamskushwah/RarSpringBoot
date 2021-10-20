package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceUpdateRequestDTO {

    @JsonProperty("user_exam_id")
    private Long userExamID;

    @JsonProperty("examiner_id")
    private Long examinerID;

    @JsonProperty("exam_id")
    private Long examID;

    @JsonProperty("resource_id")
    private Integer resourceID;

    @JsonProperty("exam_sample_id")
    private List<Long> examSampleID;

}
