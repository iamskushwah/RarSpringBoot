package com.wellsfargo.rarconsumer.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExamFields {

	@JsonProperty("field_id")
	private Long fieldID;

	@Field("suppressField")
	@JsonProperty("supress")
	private boolean supress;
	
	@JsonProperty("required")
	private boolean required;
	
	@JsonProperty("captionOverride")
	private String captionOverride;
	
	@JsonProperty("toolTipOverride")
	private String toolTipOverride;

	@Field("linecardSecIDOverride")
	@JsonProperty("linecardIDSecOverride")
	private Long linecardIDSecOverride;
	
	@JsonProperty("reportSecIDOverride")
	private Long reportSecIDOverride;
	
	@JsonProperty("sortOrder")
	private Long sortOrder;
	
	@JsonProperty("mapped")
	private Long mapped;

	@Field("status")
	@JsonProperty("fieldStatus")
	private String fieldStatus;

	@JsonProperty("deleted")
	private Boolean deleted;
}
