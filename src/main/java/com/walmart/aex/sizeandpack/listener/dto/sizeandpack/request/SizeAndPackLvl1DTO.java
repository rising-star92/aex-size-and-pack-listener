package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackLvl1DTO {
    private Long lvl1Nbr;
    private String lvl1Name;
    private List<SizeAndPackLvl2DTO> lvl2List;
}