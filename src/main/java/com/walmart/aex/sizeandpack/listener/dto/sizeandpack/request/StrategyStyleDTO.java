package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StrategyStyleDTO {
    private String styleNbr;
    private String channel;
    private List<StrategyCustomerChoiceDTO> customerChoices;
}
