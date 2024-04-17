package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walmart.aex.sizeandpack.listener.dto.DateDTO;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherClusterDTO {
    private WeatherClusterTypeDTO type;
    private Integer storeCount;
    private Integer sellingWeeks;
    private DateDTO inStoreDate;
    private DateDTO markDownDate;
    private Double lySales;
    private Long lyUnits;
    private Long onHandQty;
    private Double salesToStockRatio;
    private Double forecastedSales;
    private Long forecastedUnits;
    private Integer algoClusterRanking;
}
