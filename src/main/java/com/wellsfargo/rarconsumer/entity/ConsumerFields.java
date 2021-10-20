package com.wellsfargo.rarconsumer.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="ConsumerFields")
public class ConsumerFields {
	
	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	@JsonProperty("id")
	private String id;
	
	
	@JsonProperty("field_id")
	private long fieldID;
	
	@JsonProperty("field_name")
	private String fieldName;
	
	@JsonProperty("coll_name")
	private String collName;
	
//	@JsonProperty("linecard_section_id")
//	private long linecardSectionID;
//	
//	@JsonProperty("linecard_section")
//	private String linecardSection;
	
	@JsonProperty("control_name")
	private String controlName;
	
	@JsonProperty("sec_disc")
	private String secDisc;
	
	@JsonProperty("tool_tip")
	private String toolTip;
	
	@JsonProperty("included_in_lob_field_list")
	private boolean includedInLobFieldList;
	
	@JsonProperty("caption")
	private String caption;
	
	@JsonProperty("report_section_id")
	private long reportSectionID;
	
	@JsonProperty("required")
	private boolean required;
	
	@JsonProperty("dateAdded")
	private String dateAdded;
	
	@JsonProperty("sort_order")
	private int sortOrder;
	
	@JsonProperty("allow_caption_override")
	private boolean allowCaptionOverride;
	
	@JsonProperty("data_type")
	private String dataType;
	
	@JsonProperty("updated_date")
	private Date updatedDate;
	
	@JsonProperty("updated_by")
	private String updatedBy;
	
	@JsonProperty("alias")
	private String alias;
	
	@JsonProperty("section_id")
	private long sectionID;
	
	@JsonProperty("section")
	private String section;
	

}
