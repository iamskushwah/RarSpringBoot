package com.wellsfargo.rarconsumer.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectMasterDataResponseDTO {

	private long projectID;
	private String projectLUName;
	private long eicID;
	private String examinerName;
}
