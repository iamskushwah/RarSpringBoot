package com.wellsfargo.rarconsumer.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ConsumerLinecardSection")
public class LinecardSections {
	@Id
	private String id;
	private long sectionID;
	private String section;
	private String secDesc;
	private String displayTemplate;
	private String printTemplate;
	private Date updatedDate;
	private String updatedBy;
}
