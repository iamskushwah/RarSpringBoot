package com.wellsfargo.rarconsumer.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyLevelRemoveRequest {
	
	@JsonProperty("keyDuplicateData")
    private List<Map<String,String>> keyDuplicateData;

	@JsonProperty("examID")
    private int examID;
	
	@JsonProperty("excelSheetName")
    private String excelSheetName;
	
	@JsonProperty("keyColumns")
    private String keyColumns;

}
