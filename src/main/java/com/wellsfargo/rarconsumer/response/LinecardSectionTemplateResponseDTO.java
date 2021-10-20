package com.wellsfargo.rarconsumer.response;

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
public class LinecardSectionTemplateResponseDTO {
	private Long sectionID;
	private Long examID;
	private Boolean saveForDisplay = false;
	private String configuration;
	private Boolean saveForPrint = false;
}
