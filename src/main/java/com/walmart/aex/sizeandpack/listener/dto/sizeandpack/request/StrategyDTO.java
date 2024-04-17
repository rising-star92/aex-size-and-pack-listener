package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StrategyDTO {
    private List<WeatherClusterDTO> weatherClusters;
    private List<FixtureDTO> fixture;
}
