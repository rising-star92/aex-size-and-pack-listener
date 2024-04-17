package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SizeAndPackLvl2DTO {
    private Long lvl2Nbr;
    private String lvl2Name;
    private List<SizeAndPackLvl3DTO> lvl3List;
}
