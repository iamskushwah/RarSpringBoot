package com.wellsfargo.rarconsumer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="SystemLookup")
public class LookupValue {

    @JsonProperty("lookup_id")
    private long lookupID;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("sort_order")
    private int sortOrder;

    @JsonProperty("value1")
    private String value1;
}
