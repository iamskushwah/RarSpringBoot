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
public class KeyLevelDeleteData {
	
	@JsonProperty("keyColumns")
    private Map<String,String> keyColumns;

	@JsonProperty("objectsIds")
    private List<String> objectsIds;
	
	@JsonProperty("canDelete")
    private Boolean canDelete;

}
