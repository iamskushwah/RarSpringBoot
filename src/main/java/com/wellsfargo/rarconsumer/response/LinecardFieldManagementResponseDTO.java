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
public class LinecardFieldManagementResponseDTO {

    @JsonProperty("result")
    private List<LinecardFieldManagementDTO> result;
}
