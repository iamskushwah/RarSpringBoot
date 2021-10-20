package com.wellsfargo.rarconsumer.entity;

import org.springframework.data.annotation.Id;

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
public class RowLevelDuplicatesResponse {

	@Id
	@JsonProperty("id")
	private String id;
	
	
	@JsonProperty("rec_id")
	private String recID;
	
	@JsonProperty("row_hash")
	private String rowHash;
	
	@JsonProperty("key_hash")
	private String keyHash;
	
//	@JsonProperty("name")
//	private String name;
//	
//	@JsonProperty("city")
//	private String city;
//	
//	@JsonProperty("age")
//	private int age;
}
