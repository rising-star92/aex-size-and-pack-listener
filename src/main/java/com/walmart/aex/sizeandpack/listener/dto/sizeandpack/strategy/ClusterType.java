package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterType {
    private Integer analyticsClusterId;
    private String analyticsClusterDesc;
}
