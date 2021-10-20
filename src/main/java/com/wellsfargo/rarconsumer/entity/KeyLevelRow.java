package com.wellsfargo.rarconsumer.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeyLevelRow {

	
	@JsonProperty("rec_id")
	private String recID;
	
	@JsonProperty("row_hash")
	private String rowHash;
	
	@JsonProperty("key_hash")
	private String keyHash;
	
	@JsonProperty("deleted")
	private boolean deleted;
	
	@Field("columns")
	@JsonProperty("columns")
	private List<KeyLevelColumns> columns;
}
