package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DateDTO {
    private String fiscalWeekDesc;
    private Long wmYearWeek;
    private Long dwWeekId;
}