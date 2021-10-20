package com.wellsfargo.rarconsumer.entity;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompleteMapping {

	@JsonProperty("data_dictionary_list")
	private List<DataDictionaryDTO> dataDictionaryList;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
}
