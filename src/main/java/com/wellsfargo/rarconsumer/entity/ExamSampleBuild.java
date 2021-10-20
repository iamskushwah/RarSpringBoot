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
@Document(collection="ConsumerExamSampleBuild")
public class ExamSampleBuild {

    @Id
    private String id;
    private Long sampleID;
    private Long sampleSize;
    private Date buildDate;
    private Long sampleBuildID;
    private String mode;
    private Date updatedDate;
    private String updatedBy;
    private String editBy;
    private String comment;
}
