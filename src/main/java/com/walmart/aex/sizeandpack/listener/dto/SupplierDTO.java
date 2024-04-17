package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SupplierDTO {
    private Long supplierId;
    private Long supplier8Number;
    private String supplierName;
    private String supplierType;
    private Long supplierNumber;
}
