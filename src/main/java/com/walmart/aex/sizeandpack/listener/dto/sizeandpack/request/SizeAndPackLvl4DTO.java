package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackLvl4DTO {
	private Long lvl4Nbr;
	private String lvl4Name;
    private List<SizeAndPackFinelinesDTO> finelines;
}