package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackPayloadDTO {
    private Long planId;
    private String planDesc;
    private Long lvl0Nbr;
    private String lvl0Name;
    private List<SizeAndPackLvl1DTO> lvl1List;
}
