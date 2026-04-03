package com.czertainly.api.model.connector.cryptography.key;

import com.czertainly.api.model.common.enums.cryptography.KeyFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Response DTO returned by the connector after exporting a private key.
 *
 * <p>The connector returns the raw private key bytes in the declared {@link KeyFormat}. The platform assembles the final
 * PKCS#12 bundle from this material and the stored certificate chain and public key.</p>
 *
 * <p><strong>Expected format:</strong> {@link KeyFormat#PRKI} (DER-encoded unencrypted PrivateKeyInfo). The platform does
 * not support decrypting wrapped key material ({@link KeyFormat#EPRKI}) returned by the connector.</p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExportKeyResponseDto {

    @NotNull
    @Schema(description = "Key format of the returned material. Must be PrivateKeyInfo (PRKI).",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = "PrivateKeyInfo")
    private KeyFormat keyFormat;

    @NotBlank
    @Schema(description = "Base64-encoded private key material in the format declared by keyFormat",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyData;
}
