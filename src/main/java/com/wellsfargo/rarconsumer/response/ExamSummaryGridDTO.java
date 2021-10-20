package com.wellsfargo.rarconsumer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExamSummaryGridDTO {

	private Long examId;
	private String examinerName;
	private Long examinerCompletedCountSum;
	private Long examinerWIPCountSum;
	private Long overturnCountSum;
	private Long examinerAgreeCountSum;
	private Long examinerDisagreeCountSum;
	private Long finalAgreeCountSum;
	private Long finalDisagreeCountSum;
}
