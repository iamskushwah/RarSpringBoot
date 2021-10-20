package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceNameDTO {

    @JsonProperty("id")
    private Long examinerID;

    @JsonProperty("name")
    private String userName;
}
