package com.wellsfargo.rarconsumer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RowLevelDuplicates2 {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("exam_id")
	private long examID;
	
	@JsonProperty("rec_id")
	private String recID;
	
	@JsonProperty("row_hash")
	private String rowHash;
	
	@JsonProperty("key_hash")
	private String keyHash;
	
	@JsonProperty("fname")
	private String fname;
	
	
	@JsonProperty("fvalue")
	private String fvalue;
	
	@JsonProperty("column")
	private String column;
}
