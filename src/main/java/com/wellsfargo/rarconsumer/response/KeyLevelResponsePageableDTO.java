package com.wellsfargo.rarconsumer.response;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wellsfargo.rarconsumer.entity.KeyLevelRow;
import com.wellsfargo.rarconsumer.model.ExamResponseDTO;

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
public class KeyLevelResponsePageableDTO {

	 @JsonProperty("result")
	    private List<KeyLevelRow> result;

	    @JsonProperty("pageable")
	    private Pageable pageable;

	    @JsonProperty("total_pages")
	    private Integer totalPages;

	    @JsonProperty("total_records")
	    private Long totalElements;
}
