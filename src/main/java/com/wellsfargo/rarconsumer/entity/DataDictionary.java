package com.wellsfargo.rarconsumer.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerExamAppDictionary")
public class DataDictionary {
	
	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@Field("batchExcelFileName")
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
	@Field("sourceColumnName")
	@JsonProperty("source_column")
	private String sourceColumn;
	
	@Field("targetColumnName")
	@JsonProperty("target_column")
	private String targetColumn;

	@Field("fileSeq")
	@JsonProperty("excel_sequence_number")
	private long excelSequenceNumber;
	
	@JsonProperty("field_id")
	private Long fieldID;
	

}
