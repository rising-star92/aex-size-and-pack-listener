package com.walmart.aex.sizeandpack.listener.dto.quote;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
public class PlmAcceptedQuote {

    private BigInteger quoteId;

    private List<String> sizes;

    private BigDecimal firstCost;

    private BigDecimal landedCost;

    private String vsn;

    private BigInteger factoryId;

    private Long supplierNbr;

    private Long supplier8Nbr;

    private Long supplier9Nbr;

    private String supplierName;

    private String supplierType;

}
