package com.walmart.aex.sizeandpack.listener.dto.quote;

import lombok.Data;

import java.util.List;

@Data
public class PlmAcceptedQuoteFineline {

    private Long planId;

    private Integer lvl0Nbr;

    private Integer lvl1Nbr;

    private Integer lvl2Nbr;

    private Integer lvl3Nbr;

    private Integer lvl4Nbr;

    private Integer finelineNbr;

    private List<PlmAcceptedQuoteStyle> plmAcceptedQuoteStyles;

}
