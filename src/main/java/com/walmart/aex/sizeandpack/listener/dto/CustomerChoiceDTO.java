package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerChoiceDTO {
	private String ccId;
    private String altCcDesc;
    private String channel;
    private MetricsDTO metrics;
}