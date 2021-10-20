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
public class getEicNameByExamIdResponse {

	private long projectId;
	private String eicName;
	private String projectLUName;
	private String result;
}
