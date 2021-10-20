package com.wellsfargo.rarconsumer.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="ConsumerLinecardSection")
public class ConsumerLinecardSection {

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("section_id")
	private long sectionID;
	
	@JsonProperty("section")
	private String section;
	
	@JsonProperty("sec_desc")
	private String secDesc;
	
	@JsonProperty("updated_date")
	private Date updatedDate;
	
	@JsonProperty("updated_by")
	private String updatedBy;
}
