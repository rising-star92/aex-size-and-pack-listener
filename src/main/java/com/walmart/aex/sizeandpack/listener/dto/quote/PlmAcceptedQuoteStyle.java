package com.walmart.aex.sizeandpack.listener.dto.quote;

import lombok.Data;

import java.util.List;

@Data
public class PlmAcceptedQuoteStyle {

    private String styleNbr;

    private List<PlmAcceptedQuoteCc> plmAcceptedQuoteCcs;

}
