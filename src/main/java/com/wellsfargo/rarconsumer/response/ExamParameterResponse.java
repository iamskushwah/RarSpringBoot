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
public class ExamParameterResponse {

	private String examName;
	private String examType;
	private long projectID;
	private long examTypeID;
}
