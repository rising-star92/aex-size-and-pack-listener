package com.walmart.aex.sizeandpack.listener.dto.midas.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreSizeProfilePayloadDTO {
    private StoreSizeProfileResultDTO result;
}
