package com.wellsfargo.rarconsumer.entity;

import java.time.LocalDateTime;
import java.util.List;

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
public class AuditHistory {
	
	@JsonProperty("status")
	private String status;
	
	@Field("_updatedBy")
	@JsonProperty("updated_by")
	private String updatedBy;
	
	@Field("_updatedTime")
	@JsonProperty("updated_time")
	private LocalDateTime updatedTime;

}
