package com.wellsfargo.rarconsumer.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.model.ExamResponseDTO;
import lombok.*;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponsePageableDTO {

    @JsonProperty("result")
    private List<ExamResponseDTO> result;

    @JsonProperty("pageable")
    private Pageable pageable;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_records")
    private Long totalElements;
}
