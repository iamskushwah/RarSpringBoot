package com.wellsfargo.rarconsumer.entity;

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
@Document(collection="ConsumerExaminerSettings")

public class ExaminerSettings {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("examiner_settings_id")
    private int examinerSettingsID;

    @JsonProperty("examiner_id")
    private long examinerID;

    @JsonProperty("last_exam_id")
    private long lastExamID;

    @JsonProperty("last_line_card_id")
    private String lastLinecardID;

    @JsonProperty("field_mgt_filter")
    private int fieldMgtFilter;

    @JsonProperty("line_card_redirect")
    private boolean linecardRedirect;

    @JsonProperty("warning")
    private String warning;

}
