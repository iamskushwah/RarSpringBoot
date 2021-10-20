package com.wellsfargo.rarconsumer.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerExamAppAuditLog")
public class AuditColl {

	@Id
	@JsonProperty("id")
	private String id;

	@JsonProperty("exam_id")
	private long examID;

	@Field("batchExcelFileName")
	@JsonProperty("excel_sheet_name")
	private String excelSheetName;
	
	@JsonProperty("current_status")
	private String currentStatus;
	
	@JsonProperty("updated_by")
	private String updatedBy;

	@Field("updatedTime")
	@JsonProperty("updated_date")
	private LocalDateTime currentUpdatedDate;
	
	@JsonProperty("number_of_record_loaded")
	private long numberOfRecordLoaded ;

	@Field("fileSeq")
	@JsonProperty("excel_sequence_number")
	private long excelSequenceNumber ;
	
	@JsonProperty("audit_history")
	private List<AuditHistory> auditHistory;
	
	@JsonProperty("key_columns")
	private String keyColumns;
	
	@JsonProperty("src_excel_file_name")
	private String srcExcelFileName;
	
}
