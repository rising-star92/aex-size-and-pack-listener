package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeaderDTO {

    private String type;
    private String source;
    private Long timeStamp;
    private Integer version;
    private ChangeScopeDTO changeScope;
}
