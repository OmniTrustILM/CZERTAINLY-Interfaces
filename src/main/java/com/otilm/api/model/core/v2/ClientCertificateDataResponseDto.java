package com.otilm.api.model.core.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.otilm.api.model.common.UuidDto;
import com.otilm.api.model.core.logging.Loggable;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Response for the queued client certificate operations. Carries the certificate UUID; the signed certificate is not
 * returned here — retrieve it from the certificate detail.
 */
@Getter
@Setter
public class ClientCertificateDataResponseDto implements Loggable {

    @Schema(description = "Base64-encoded signed certificate. The issuance operations complete asynchronously "
            + "and leave this empty; register leaves it empty too unless the authority returns certificate data "
            + "synchronously. Retrieve the signed certificate from the certificate detail once the certificate "
            + "reaches the ISSUED state.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String certificateData;

    @Schema(description = "UUID of Certificate", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @ArraySchema(arraySchema = @Schema(
            description = "Request-attribute policy warnings raised while validating an externally supplied CSR "
                    + "under lenient request validation. Empty under strict, where a violation is rejected with "
                    + "422 instead of accepted with warnings.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED))
    private List<String> requestAttributeWarnings = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "ACME External Account Binding key identifier (the pre-registered certificate UUID). "
            + "Present only in the response to a registration that requested generateEabCredential.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String eabKid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "ACME External Account Binding HMAC key, base64url-encoded. Present only in the response "
            + "to a registration that requested generateEabCredential and returned exactly once; the platform "
            + "stores it as the registration challenge and cannot show it again.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String eabHmacKey;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("certificateData", certificateData)
                .append("certificateUuid", uuid)
                .append("requestAttributeWarnings", requestAttributeWarnings)
                .append("eabKid", eabKid)
                .toString();
    }

    @Override
    public Serializable toLogData() {
        return new UuidDto(uuid);
    }

    @Override
    public List<String> toLogResourceObjectsNames() {
        return List.of();
    }

    @Override
    public List<UUID> toLogResourceObjectsUuids() {
        return List.of(UUID.fromString(uuid));
    }
}
