package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PayloadDTO {

	private Long planId;
    private String planDesc;
    private Long lvl0Nbr;
    private String lvl0Desc;
    private Long lvl1Nbr;
    private String lvl1Desc;
    private Long lvl2Nbr;
    private String lvl2Desc;
    private Long lvl3Nbr;
    private Long lvl4Nbr;
    private Lvl3DTO lvl3;
}
