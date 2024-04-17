package com.walmart.aex.sizeandpack.listener.dto.sizeandpack.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeAndPackDeletePayloadDTO {
    private SizeAndPackStrongKeyDTO strongKey;
    private SizeAndPackPayloadDTO sizeAndPackPayloadDTO;
}
