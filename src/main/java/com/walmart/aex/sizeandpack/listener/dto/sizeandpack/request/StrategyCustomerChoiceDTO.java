package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StrategyCustomerChoiceDTO {
    private String ccId;
    private String colorName;
    private String channel;
    private StrategyDTO strategy;
}
