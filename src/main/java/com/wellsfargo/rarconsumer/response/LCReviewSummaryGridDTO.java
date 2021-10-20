package com.wellsfargo.rarconsumer.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LCReviewSummaryGridDTO {
	private Long examSampleID;
	private Long examId;
	private Long examinerID;
	private String eicComments;
	private String sampleName;
	private String borrower1;
	private Boolean complete;
	private Long jobID;
	private String applicationNumber;
	private String accountNumber;
	private String examinerFullName;
	private String examinerConclusion;
	private String finalConclusion;
	private String linecardID;
	private Boolean overturn;
	private Boolean selectedForPrinting;
	private Boolean eicReviewed;
	private Date completionDate;
}
