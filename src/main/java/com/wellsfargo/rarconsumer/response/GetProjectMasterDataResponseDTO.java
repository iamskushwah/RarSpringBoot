package com.wellsfargo.rarconsumer.response;

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
public class GetProjectMasterDataResponseDTO {

	private long projectID;
	private String result;
}
