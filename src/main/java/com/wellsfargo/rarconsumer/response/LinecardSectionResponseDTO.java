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
public class LinecardSectionResponseDTO {
	private Long sectionID;
	private Long examID;
	private String section;
	private String secDesc;
	private Boolean suppressSection;
	private Long linecardOrderOverride;
	private Long reportOrderOverride;
	private Boolean pgBrkOverride;
	private String displayTemplate;
	private String printTemplate;
}
