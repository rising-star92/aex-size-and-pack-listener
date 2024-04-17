package com.walmart.aex.sizeandpack.listener.dto.quote;

import lombok.Data;

import java.util.List;

@Data
public class PlmAcceptedQuoteCc {

    private String customerChoice;

    private List<PlmAcceptedQuote> plmAcceptedQuotes;

}
