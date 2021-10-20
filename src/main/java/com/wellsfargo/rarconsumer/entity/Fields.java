package com.wellsfargo.rarconsumer.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerFields")
public class Fields {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("field_id")
    private long fieldID;

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("coll_name")
    private String collName;

    @JsonProperty("section_id")
    private long sectionID;

    @JsonProperty("section")
    private String section;

    @JsonProperty("control_name")
    private String controlName;

    @JsonProperty("tool_tip")
    private String toolTip;

    @JsonProperty("included_in_lob_field_list")
    private boolean includedInLOBFieldList;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("report_section_id")
    private long reportSectionID;

    @JsonProperty("required")
    private boolean required;

    @JsonProperty("date_added")
    private String dateAdded;

    @JsonProperty("sort_order")
    private long sortOrder;

    @JsonProperty("allow_caption_override")
    private boolean allowCaptionOverride;

    @JsonProperty("table_name")
    private String tableName;

}
