package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LinecardSectionRequestDTO {
	@JsonProperty("exam_id")
	private Long examID;
	private Long sectionID;
	private String sectionDesc;
	private String sectionName;
}
