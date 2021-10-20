package com.wellsfargo.rarconsumer.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSummaryGridDTO {
	private Long userExamID;
    private String lastName;    
    private String firstName;
    private List<String> samples;
    private Long role;
}
