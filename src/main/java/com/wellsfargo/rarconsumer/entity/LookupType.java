package com.wellsfargo.rarconsumer.entity;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.response.LookupTypeResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="SystemLookup")
public class LookupType {
	
	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("lookup_type_id")
	private long lookupTypeID;
	
	@JsonProperty("lookup_type")
	private String lookupType;
	
	@JsonProperty("lookup_type_value")
	private String lookupTypeValue;
	
	@JsonProperty("editable")
	private boolean editable;
	
	@JsonProperty("lookup_value")
	private List<LookupTypeResponseDTO> lookupValue;
	
	@JsonProperty("lookup_id")
	private long lookupID;
	
	@JsonProperty("active")
	private boolean active;
	
	@JsonProperty("value2")
	private String value2;
	
	@JsonProperty("value1")
	private String value1;
	
	
}
