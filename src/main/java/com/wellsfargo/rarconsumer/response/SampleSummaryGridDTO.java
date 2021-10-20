package com.wellsfargo.rarconsumer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SampleSummaryGridDTO {

	private Long examId;
	private String sampleName;
	private String subName;
	private Long sampleCountSum;
	private Long linecardRemainingCountSum;
	private Long linecardWIPCountSum;
	private Long linecardCompletedCountSum;
	private Long linecardAgreedCountSum;
	private Long linecardFinalAgreeCountSum;
	private Long linecardDisagreeCountSum;
	private Long linecardFinalDisagreeCountSum;
	private Boolean active;
	private Long sortOrder;
	private Long examSampleId;
}
