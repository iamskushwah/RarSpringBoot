package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceSamplesDTO {

    @JsonProperty("id")
    private Long examSampleID;

    @JsonProperty("sample_name")
    private String samples;

    @JsonProperty("sub_name")
    private String subName;

    @JsonProperty("sort_order")
    private Integer sortOrder;
}
