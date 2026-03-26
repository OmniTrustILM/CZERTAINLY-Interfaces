package com.czertainly.api.model.client.signing.profile;

import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@ParameterObject
public class SigningProfileForVersionDto {
    private Integer version;
}
