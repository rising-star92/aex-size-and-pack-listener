package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy.SizeAndPackStrategyDTO;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackCustomerChoiceDTO {
    private String ccId;
    private String altCcDesc;
    private String colorName;
    private String channel;
    private String colorFamily;
    private SizeAndPackStrategyDTO sizeAndPackStrategy;
    private ConstraintsDTO constraints;
}
