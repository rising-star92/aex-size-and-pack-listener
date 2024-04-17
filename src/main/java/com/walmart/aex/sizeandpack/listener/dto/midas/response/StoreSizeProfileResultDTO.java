package com.walmart.aex.sizeandpack.listener.dto.midas.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreSizeProfileResultDTO {
    private List<StoreSizeResponseDTO> response;
}
