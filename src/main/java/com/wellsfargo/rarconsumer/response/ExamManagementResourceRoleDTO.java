package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceRoleDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("role_name")
    private String role;
}
