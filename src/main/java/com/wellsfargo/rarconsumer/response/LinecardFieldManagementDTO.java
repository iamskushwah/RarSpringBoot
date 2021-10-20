package com.wellsfargo.rarconsumer.response;

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
public class LinecardFieldManagementDTO {

    private Long fieldID;
    private String caption;
    private String fieldName;
    private Long mapped;
    private String status;
    private String collName;
    private String section;
    private String captionOverride;
    private String toolTip;
    private Long sortOrder;
}
