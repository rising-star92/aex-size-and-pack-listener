package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeCluster {
    private ClusterType type;
    private List<SizeProfileDTO> sizeProfiles;
}
