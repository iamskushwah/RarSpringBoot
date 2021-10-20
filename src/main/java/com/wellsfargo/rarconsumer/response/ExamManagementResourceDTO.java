package com.wellsfargo.rarconsumer.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceDTO {

    @JsonProperty("id")
    private Long userExamID;

    @JsonProperty("role")
    private String role;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("samples")
    private List<String> samples;
    
    @JsonProperty("exam_id")
    private Long examId;

}
