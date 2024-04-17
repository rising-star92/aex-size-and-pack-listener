package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy.SizeAndPackStrategyDTO;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackStyleDTO {
    private String styleNbr;
    private String altStyleDesc;
    private String channel;
    private List<SizeAndPackCustomerChoiceDTO> customerChoices;
    private SizeAndPackStrategyDTO sizeAndPackStrategy;
}
