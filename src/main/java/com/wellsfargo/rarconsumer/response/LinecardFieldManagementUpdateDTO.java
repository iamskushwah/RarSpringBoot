package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinecardFieldManagementUpdateDTO {

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("section_id")
    private Long SectionId;

    @JsonProperty("ection")
    private String Section;

    @JsonProperty("tool_tip")
    private String toolTip;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("status")
    private String Status;
}
