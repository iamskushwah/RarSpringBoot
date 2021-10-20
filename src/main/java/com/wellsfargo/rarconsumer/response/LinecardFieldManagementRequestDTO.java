package com.wellsfargo.rarconsumer.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinecardFieldManagementRequestDTO {

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("section_id")
    private Long SectionId;

    @JsonProperty("section")
    private String Section;

    @JsonProperty("required")
    private Boolean required;

    @JsonProperty("tool_tip")
    private String toolTip;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("sort_order")
    private Long sortOrder;

}
