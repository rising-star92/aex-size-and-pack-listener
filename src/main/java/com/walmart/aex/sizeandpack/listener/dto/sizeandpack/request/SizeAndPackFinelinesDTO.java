package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy.SizeAndPackStrategyDTO;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackFinelinesDTO {
    private Long finelineNbr;
    private String finelineName;
    private String altFinelineName;
    private String channel;
    private List<SizeAndPackStyleDTO> styles;
    private SizeAndPackStrategyDTO sizeAndPackStrategy;
}
