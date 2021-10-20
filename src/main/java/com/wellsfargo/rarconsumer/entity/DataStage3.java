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
@Document(collection="ConsumerExamAppStage")
public class DataStage3 {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@Field("batchExcelFileName")
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
	@JsonProperty("row_hash")
	private String rowHash;
	
	@JsonProperty("key_hash")
	private String keyHash;
	
	@JsonProperty("delete_flag")
	private String deleteFlag;
	
	@Field("columns")
	@JsonProperty("data")
	private List<Stage1Column> data;
}
