package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstraintsDTO {
	private ColorCombinationConstraintsDTO colorCombinationConstraints;
	private FinelineLevelConstraintsDTO finelineLevelConstraints;
}
