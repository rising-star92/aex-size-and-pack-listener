package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TypeSpecificDTO {
    private String traitChoice;
    private String preferredFixtureType;
    private String preferredFixtureType2;
    private String preferredFixtureType3;
    private String preferredFixtureType4;
    private List<ColorsDTO> colors;
    private List<SupplierDTO> suppliers;
}
