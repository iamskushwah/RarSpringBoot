package com.wellsfargo.rarconsumer.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyLevelColumns {

	@Field("fieldName")
	@JsonProperty("fname")
	private String fname;
	
	@Field("fieldValue")
	@JsonProperty("fvalue")
	private String fvalue;
	
//	@JsonProperty("uiName")
//	private String uiName;
}
