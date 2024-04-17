package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy.SizeAndPackStrategyDTO;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackLvl3DTO {
	private Long lvl3Nbr;
    private String lvl3Name;
    private SizeAndPackStrategyDTO sizeAndPackStrategy;
    private List<SizeAndPackLvl4DTO> lvl4List;
}
