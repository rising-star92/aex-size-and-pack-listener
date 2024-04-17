package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineSizeProfileRequestDTO {

    private String baseItemId;
    private String colorFamily;
    private String season;
    private Long deptNbr;
}
