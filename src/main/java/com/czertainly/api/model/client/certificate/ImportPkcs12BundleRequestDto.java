package com.czertainly.api.model.client.certificate;

import com.czertainly.api.model.client.attribute.RequestAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO to import a certificate and its key pair from a PKCS#12 bundle.
 *
 * <p>The platform parses the bundle, stores the certificate chain, and delegates key pair storage to the connector
 * identified by the supplied token instance and token profile.</p>
 */
@Data
public class ImportPkcs12BundleRequestDto {

    // ── Bundle ────────────────────────────────────────────────────────────────

    @NotNull
    @NotBlank
    @Schema(description = "Base64-encoded PKCS#12 bundle",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String pkcs12Data;

    @NotNull
    @Schema(description = "Passphrase protecting the PKCS#12 bundle. Can be empty.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY)
    private String passphrase;

    @Schema(description = "Alias of the entry to import. Required when the bundle contains more than one entry; " +
            "optional when exactly one entry is present.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String alias;

    // ── Naming ────────────────────────────────────────────────────────────────

    @NotNull
    @NotBlank
    @Schema(description = "Display name for the certificate object in the platform",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String certificateName;

    @NotNull
    @NotBlank
    @Schema(description = "Display name for the cryptographic key object in the platform",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyName;

    @Schema(description = "Optional description applied to both the certificate and the key",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    // ── Key storage ───────────────────────────────────────────────────────────

    @NotNull
    @Schema(description = "UUID of the Token Instance that will store the key pair",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID tokenInstanceUuid;

    @NotNull
    @Schema(description = "UUID of the Token Profile governing the key pair",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID tokenProfileUuid;

    @Schema(description = "Whether the private key may be exported after the import. " +
            "Defaults to false. Cannot be changed to true after the key is created.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean exportable = false;

    @Schema(description = "Enabled status of created keys. True = Enabled, False = Disabled. Defaults to false.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean enabled = false;

    // ── Metadata ──────────────────────────────────────────────────────────────

    @Schema(description = "Group associations for both the certificate and the key",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<UUID> groupUuids;

    @Schema(description = "Custom attributes",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> customAttributes;

    @Schema(description = "Connector-specific import attributes as declared by the connector's " +
            ".../import/attributes endpoint (e.g. target slot, key label in the HSM).",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<RequestAttribute> importAttributes;
}
