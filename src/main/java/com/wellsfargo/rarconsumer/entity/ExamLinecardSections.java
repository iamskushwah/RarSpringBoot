package com.wellsfargo.rarconsumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExamLinecardSections {
	
	@JsonProperty("section_id")
	private Long sectionID;

	@Field("suppressSection")
	@JsonProperty("suppress")
	private Boolean suppress;

	@Field("linecardOrderOverride")
	@JsonProperty("linecard_override")
	private Long linecardOverride;
	
	@JsonProperty("report_order_override")
	private Long reportOrderOverride;
	
	@JsonProperty("pg_brk_override")
	private Boolean pgBrkOverride;
}
