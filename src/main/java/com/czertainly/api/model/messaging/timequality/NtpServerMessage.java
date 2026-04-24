package com.czertainly.api.model.messaging.timequality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "NtpServerMessage", description = "Per-server NTP measurement result carried in a time quality result message")
public class NtpServerMessage {

    @Schema(description = "NTP server hostname or IP address", requiredMode = Schema.RequiredMode.REQUIRED)
    private String host;

    @Schema(description = "Whether the server was reachable during this check", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean reachable;

    @Schema(description = "Measured clock offset from this server in milliseconds; null when server was unreachable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double offsetMs;

    @Schema(description = "Round-trip time to this server in milliseconds; null when server was unreachable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double rttMs;

    @Schema(description = "NTP stratum of this server; null when server was unreachable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer stratum;

    @Schema(description = "Clock precision of this server in milliseconds; null when server was unreachable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double precisionMs;
}
