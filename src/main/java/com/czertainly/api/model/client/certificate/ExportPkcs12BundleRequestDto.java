package com.czertainly.api.model.client.certificate;

import com.czertainly.api.model.client.attribute.RequestAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Request DTO to export a certificate together with its public and private keys as a PKCS#12 bundle.
 *
 * <p>Only available when the certificate has an associated private key marked as exportable.</p>
 */
@Data
public class ExportPkcs12BundleRequestDto {

    @NotNull
    @Schema(description = "Passphrase to protect the assembled PKCS#12 bundle. Can be empty.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passphrase;

    @Schema(description = "Include the full certificate chain in the bundle. Defaults to true.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean includeCertificateChain = true;

    @Schema(description = "Connector-specific export attributes as declared by the connector's " +
            ".../export/attributes endpoint. May be absent for connectors that require no additional input.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> exportAttributes;
}
