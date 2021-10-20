package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.entity.DataDictionary;

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
public class DataDictionaryResponseDTO {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("exam_id")
	private Long examID;
	
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
	@JsonProperty("source_column")
	private String sourceColumn;
	
	@JsonProperty("target_column")
	private String targetColumn;
	
	@JsonProperty("field_id")
	private Long fieldID;
}
