package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StrongKeyFinelineDTO {
    private Long finelineId;
    private String finelineName;
    private String altFinelineName;
    private String channel;
    private String noOfNewStyles;
    private String noOfNewCCs;
    private String isNew;
    private List<StrongKeyStyleDTO> styles;
    
}
