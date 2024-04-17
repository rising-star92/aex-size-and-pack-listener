package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinelineLevelConstraintsDTO {
    private Integer maxPacks;
    private Integer maxUnitsPerPack;
}
