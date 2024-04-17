package com.walmart.aex.sizeandpack.listener.dto;

import lombok.Data;

import java.util.List;

@Data
public class FinelineDTO {

    private Integer finelineNbr;
    private List<StyleDTO> styles;
}
