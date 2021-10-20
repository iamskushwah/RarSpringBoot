package com.wellsfargo.rarconsumer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamManagementResourceResponseDTO {

    @JsonProperty("result")
    private List<ExamManagementResourceDTO> result;
}
