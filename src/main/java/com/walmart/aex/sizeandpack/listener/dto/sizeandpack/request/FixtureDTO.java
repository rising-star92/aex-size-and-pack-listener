package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FixtureDTO {
    private Integer orderPref;
    private String type;
}
