package com.czertainly.api.model.core.signing.signingprofile;

import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@ParameterObject
public class SigningProfileForVersionDto {
    private Integer version;
}
