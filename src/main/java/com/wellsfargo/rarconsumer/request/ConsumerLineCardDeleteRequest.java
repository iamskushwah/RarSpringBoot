package com.wellsfargo.rarconsumer.request;

import java.util.List;

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
public class ConsumerLineCardDeleteRequest {
	
	@JsonProperty("linecardIDs")
    private List<String> linecardIDs;
	
	@JsonProperty("comment")
	private String comment;

}
