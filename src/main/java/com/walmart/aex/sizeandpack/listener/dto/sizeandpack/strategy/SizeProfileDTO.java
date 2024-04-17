package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeProfileDTO {
    private Integer ahsSizeId;
    private String sizeDesc;
    private Double sizeProfilePrcnt;
    private Double adjustedSizeProfile;
    private Integer isEligible;
}
