package com.wellsfargo.rarconsumer.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataDictionaryDTO {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("field_id")
	private long  fieldID;
	
	@JsonProperty("source_column")
	private String sourceColumn;
	
	@JsonProperty("target_column")
	private String targetColumn;
}
