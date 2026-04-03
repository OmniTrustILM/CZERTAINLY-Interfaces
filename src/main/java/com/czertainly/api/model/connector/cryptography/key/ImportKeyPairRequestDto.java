package com.czertainly.api.model.connector.cryptography.key;

import com.czertainly.api.model.client.attribute.RequestAttribute;
import com.czertainly.api.model.common.enums.cryptography.KeyFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Request DTO to import a key pair into a connector token.
 *
 * <p>The platform parses the PKCS#12 bundle, stores the certificate chain itself, and forwards the extracted key pair
 * material to the connector. The public key is extracted from the certificate's SubjectPublicKeyInfo; the private key
 * is extracted from the bundle's encrypted key bag. The connector is responsible for importing the key pair into
 * the token and returning the resulting key pair data (see {@link KeyPairDataResponseDto}).</p>
 *
 * <p>Connector-specific import parameters (such as target slot, label inside the HSM) are declared by the connector via
 * {@code GET /import/attributes} and forwarded here as {@code importAttributes}.</p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImportKeyPairRequestDto {

    @NotNull
    @Schema(description = "Token profile attributes for access control on the target token",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RequestAttribute> tokenProfileAttributes;

    @NotNull
    @Schema(description = "Format of the public key material provided in publicKeyData. Expected to be SubjectPublicKeyInfo (SPKI).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"SubjectPublicKeyInfo"})
    private KeyFormat publicKeyFormat;

    @NotNull
    @NotBlank
    @Schema(description = "Base64-encoded public key material in the format declared by publicKeyFormat. " +
            "Extracted by the platform from the end-entity certificate in the PKCS#12 bundle.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String publicKeyData;

    @NotNull
    @Schema(description = "Format of the private key material provided in privateKeyData. Expected to be PrivateKeyInfo (PRKI).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PrivateKeyInfo"})
    private KeyFormat privateKeyFormat;

    @NotNull
    @NotBlank
    @Schema(description = "Base64-encoded private key material in the format declared by privateKeyFormat. " +
            "Extracted by the platform from the PKCS#12 bundle supplied by the caller.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String privateKeyData;

    @Schema(description = "Connector-specific import attributes as declared by GET .../import/attributes " +
            "(e.g. target slot, key label in the HSM). Does not carry the PKCS#12 bundle or passphrase.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> importAttributes;
}
