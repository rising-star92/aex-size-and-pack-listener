package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StrategyFinelinesDTO {
    private Long finelineNbr;
    private String finelineName;
    private String traitChoice;
    private String channel;
    private List<StrategyStyleDTO> styles;
    private StrategyDTO strategy;
}
