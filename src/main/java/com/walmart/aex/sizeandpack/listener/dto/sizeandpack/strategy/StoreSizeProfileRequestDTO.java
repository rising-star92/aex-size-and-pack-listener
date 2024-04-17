package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreSizeProfileRequestDTO {
    private Long finelineNbr;
    private String colorFamily;
    private String season;
    private Long deptNbr;
}
