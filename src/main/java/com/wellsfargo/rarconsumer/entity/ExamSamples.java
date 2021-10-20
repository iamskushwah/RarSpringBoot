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
@Document(collection="ConsumerExamSample")
public class ExamSamples {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("sample_id")
    private long sampleID;

    
    @JsonProperty("sample_name")
    private String sampleName;

    @JsonProperty("sample_sub_name")
    private String sampleSubName;
    
    @Field("LOBCriteria")
    @JsonProperty("lob_criteria")
    private String lOBCriteria;

    @JsonProperty("exam_id")
    private long examID;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("sort_order")
    private int sortOrder;

    @JsonProperty("criteria")
    private String criteria;
    
    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("comment_date")
    private Date commentDate;
}
