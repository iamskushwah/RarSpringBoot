package com.wellsfargo.rarconsumer.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSamplesRequestDTO {

    @JsonProperty("sample_name")
    private String sampleName;

    @JsonProperty("sample_sub_name")
    private String sampleSubName;

    @JsonProperty("lob_criteria")
    private String LOBCriteria;

    @JsonProperty("exam_id")
    private long examID;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("sort_order")
    private int sortOrder;

    @JsonProperty("criteria")
    private String criteria;


}
