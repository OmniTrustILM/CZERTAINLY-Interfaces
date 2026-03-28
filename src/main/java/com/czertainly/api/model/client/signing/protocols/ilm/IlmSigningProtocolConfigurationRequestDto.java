package com.czertainly.api.model.client.signing.protocols.ilm;

import com.czertainly.api.model.client.attribute.RequestAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "IlmSigningProtocolConfigurationRequestDto", description = "Request to create or update an ILM Signing Protocol Configuration")
public class IlmSigningProtocolConfigurationRequestDto {

    @NotBlank
    @Schema(description = "Name of the ILM Signing Protocol Configuration", requiredMode = Schema.RequiredMode.REQUIRED, example = "ILM-Config-1")
    private String name;

    @Schema(description = "Description of the ILM Signing Protocol Configuration", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "ILM signing protocol configuration for production")
    private String description;

    @Schema(description = "UUID of the default Signing Profile", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "6b55de1c-844f-11ec-a8a3-0242ac120002")
    private UUID defaultSigningProfileUuid;

    @Schema(description = "List of Custom Attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> customAttributes = new ArrayList<>();
}
