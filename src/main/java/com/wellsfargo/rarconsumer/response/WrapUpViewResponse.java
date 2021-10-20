package com.wellsfargo.rarconsumer.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.entity.ConsumerLinecard;

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
public class WrapUpViewResponse {

	private int linecardReviewCount;
	private int missingEICCommentsCount;
	private int examSummaryCount;
	private boolean wrapUp;
}
