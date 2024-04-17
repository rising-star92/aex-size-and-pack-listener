package com.walmart.aex.sizeandpack.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StyleDTO {

	private String styleNbr;
    private String altStyleDesc;
    private String channel;
    private List<CustomerChoiceDTO> customerChoices;
}