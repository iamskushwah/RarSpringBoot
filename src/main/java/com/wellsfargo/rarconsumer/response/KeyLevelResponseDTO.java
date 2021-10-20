package com.wellsfargo.rarconsumer.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.entity.KeyLevelColumns;
import com.wellsfargo.rarconsumer.entity.KeyLevelRow;
import com.wellsfargo.rarconsumer.model.ExamResponseDTO;

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
public class KeyLevelResponseDTO {

	@JsonProperty("rec_id")
	private String recID;
	
	@JsonProperty("row_hash")
	private String rowHash;
	
	@JsonProperty("key_hash")
	private String keyHash;
	
	@JsonProperty("deleted")
	private Boolean deleted;
	
	@JsonProperty("columns")
	private List<KeyLevelColumns> columns;
}
