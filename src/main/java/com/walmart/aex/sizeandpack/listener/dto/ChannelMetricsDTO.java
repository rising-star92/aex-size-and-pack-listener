package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChannelMetricsDTO {
    private ProductAttributesDTO productAttributes;
    private FinancialAttributesDTO financialAttributes;
}
