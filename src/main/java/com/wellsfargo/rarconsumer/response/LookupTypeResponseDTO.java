package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.entity.LookupValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LookupTypeResponseDTO {

	 	@JsonProperty("lookup_id")
	    private long lookupID;

	    @JsonProperty("active")
	    private boolean isActive;

	    @JsonProperty("sort_order")
	    private int sortOrder;

	    @JsonProperty("value1")
	    private String value1;
}
