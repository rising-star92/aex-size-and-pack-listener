package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrentMetricsDTO {
    private String planComments;
    private ChannelMetricsDTO store;
    private ChannelMetricsDTO online;
}
