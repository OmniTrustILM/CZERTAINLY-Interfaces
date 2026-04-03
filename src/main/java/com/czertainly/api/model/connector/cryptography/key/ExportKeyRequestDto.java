package com.czertainly.api.model.connector.cryptography.key;

import com.czertainly.api.model.client.attribute.RequestAttribute;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Request DTO to export raw private key material from a connector token.
 *
 * <p>The connector returns the raw private key bytes (see {@link ExportKeyResponseDto});
 * the platform assembles the final PKCS#12 bundle from the certificate chain and public key.</p>
 *
 * <p>The connector may declare export-specific attributes (such as wrapping options, HSM slot)
 * via {@code GET /{keyUuid}/export/attributes}. Those attribute values are collected from
 * the caller and forwarded here as {@code exportAttributes}.</p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExportKeyRequestDto {

    @NotNull
    @Schema(description = "Token profile attributes for access control",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RequestAttribute> tokenProfileAttributes;

    @Schema(description = "Export-specific attributes declared by the connector (e.g. wrapping options, source slot). " +
            "May be absent for connectors that require no additional input.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> exportAttributes;
}
