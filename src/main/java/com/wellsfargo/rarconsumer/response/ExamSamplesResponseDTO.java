package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSamplesResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("sample_id")
    private Long sampleID;

    @JsonProperty("sample_name")
    private String sampleName;

    @JsonProperty("sample_sub_name")
    private String sampleSubName;

    @JsonProperty("lob_criteria")
    private String LOBCriteria;

    @JsonProperty("exam_id")
    private Long examID;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("sort_order")
    private Integer sortOrder;

    @JsonProperty("criteria")
    private String criteria;


}
