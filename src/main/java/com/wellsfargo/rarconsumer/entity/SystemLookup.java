package com.wellsfargo.rarconsumer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="SystemLookup")
public class SystemLookup {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("lookup_type_id")
    private long lookupTypeID;

    @JsonProperty("lookup_type")
    private String lookupType;

    @JsonProperty("lookup_type_value")
    private String lookupTypeValue;

    @JsonProperty("is_editable")
    private boolean isEditable;

    @JsonProperty("lookup_value")
    private List<LookupValue> lookupValue;

    @JsonProperty("value1_type")
    private String value1Type;

    @JsonProperty("value1_label")
    private String value1Label;

    @JsonProperty("is_visible")
    private boolean isVisible;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_date")
    private String updatedDate;

    @JsonProperty("is_add_enabled")
    private boolean isAddEnabled;
}
