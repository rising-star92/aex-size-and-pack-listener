package com.walmart.aex.sizeandpack.listener.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChangeScopeDTO {

    private List<String> updatedAttributes;
    private StrongKeyDTO strongKeys;
}
