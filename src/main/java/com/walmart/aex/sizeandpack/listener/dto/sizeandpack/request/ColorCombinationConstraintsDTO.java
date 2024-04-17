package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.walmart.aex.sizeandpack.listener.dto.SupplierDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorCombinationConstraintsDTO {
    private List<SupplierDTO> suppliers;
    private String factoryId;
    private String portOfOrigin;
    private Integer singlePackIndicator;
    private String colorCombination;
}
