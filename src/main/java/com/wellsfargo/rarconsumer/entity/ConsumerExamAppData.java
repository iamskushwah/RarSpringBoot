package com.wellsfargo.rarconsumer.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerExamAppData")
public class ConsumerExamAppData {

	@Id
	@JsonProperty("id")
	private String id;
	
	@Field("linecardID")
	@JsonProperty("linecard_id")
	private String linecardID;
	
	@JsonProperty("application_number")
	private String applicationNumber;
	
	@JsonProperty("account_number")
	private String accountNumber;
	
	@JsonProperty("borrower1")
	private String borrower1;
	
	@JsonProperty("borrower2")
	private String borrower2;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("zip")
	private String zip;
	
	@JsonProperty("product")
	private String product;
	
	@JsonProperty("original_commitment")
	private long originalCommitment;
	
	@JsonProperty("bureauScore1")
	private String bureauScore1;

	@JsonProperty("customScore1")
	private String customScore1;
	
	@JsonProperty("willingness_to_repay_debt")
	private String willingnessToRepayDebt;
	
	@JsonProperty("credit_history_comments")
	private String creditHistoryComments;
	
	@JsonProperty("occupation1")
	private String occupation1;
	
	@JsonProperty("total_income_RAR")
	private long totalIncomeRAR;
	
	@JsonProperty("occupancy_type")
	private String occupancyType;
	
	@JsonProperty("proposed_payment")
	private long proposedPayment;
	
	@JsonProperty("di_percent_lob")
	private long diPercentLOB;
	
	@JsonProperty("di_percent_rar")
	private long diPercentRAR;
	
	@JsonProperty("ability_toRepay_debt")
	private String abilityToRepayDebt;
	
	@JsonProperty("financial_comments")
	private String financialComments;
	
	@JsonProperty("exam_id")
	private int examID;
	
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
}
