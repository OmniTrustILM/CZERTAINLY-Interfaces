package com.czertainly.api.model.core.signing.signatureprofile;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonDeserialize(using = JsonDeserializer.None.class)
@Schema(name = "OneTimeKeyManagedSigningRequestDto",
        description = "Request to configure managed signing with a one-time certificate issuance")
public class OneTimeKeyManagedSigningRequestDto extends ManagedSigningRequestDto {

    @NotNull
    @Schema(description = "UUID of the RA Profile used to issue the one-time signing certificate",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID raProfileUuid;

    @NotNull
    @Schema(description = "UUID of the CSR Template used for the certificate issuance request",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID csrTemplateUuid;

    @NotNull
    @Schema(description = "UUID of the Token Profile used to store the issued certificate and key pair",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID tokenProfileUuid;

    public OneTimeKeyManagedSigningRequestDto() {
        super(ManagedSigningType.ONE_TIME_KEY);
    }
}
