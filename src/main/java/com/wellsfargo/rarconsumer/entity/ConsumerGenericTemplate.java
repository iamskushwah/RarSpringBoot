package com.wellsfargo.rarconsumer.entity;

import java.util.Date;

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
@Document(collection="ConsumerGenericTemplate")
public class ConsumerGenericTemplate {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("generic_template_id")
	private Long genericTemplateID;
	
	@JsonProperty("exam_type_id")
	private Long examTypeID;
	
	@JsonProperty("linecard_section_id")
	private Long linecardSectionID;
	
	@JsonProperty("line_card_order")
	private Long linecardOrder;
	
	@JsonProperty("report_order")
	private Long reportOrder;
	
	@JsonProperty("page_break")
	private boolean pageBreak;
	
	@JsonProperty("updated_date")
	private Date updatedDate;
	
	@JsonProperty("updated_by")
	private String updatedBy;
}
