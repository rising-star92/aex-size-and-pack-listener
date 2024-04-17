package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackStrategyDTO {
    private List<SizeCluster> storeSizeClusters;
    private List<SizeCluster> onlineSizeClusters;
}
