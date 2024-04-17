package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Lvl3DTO {

    private String lvl3Name;
    private Long lvl3Nbr;
    private Lvl4DTO lvl4;
}
