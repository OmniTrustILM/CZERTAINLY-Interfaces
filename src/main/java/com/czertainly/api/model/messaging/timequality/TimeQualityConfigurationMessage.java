package com.czertainly.api.model.messaging.timequality;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "TimeQualityConfigurationMessage", description = "NTP-based time quality configuration carried within a configuration snapshot")
public class TimeQualityConfigurationMessage {

    @Schema(description = "Unique identifier of the time quality configuration", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "Display name of the time quality configuration", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "List of NTP server addresses used for time quality checks", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"pool.ntp.org\", \"time.google.com\"]")
    private List<String> ntpServers;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Interval between NTP checks, in ISO 8601 duration format", requiredMode = Schema.RequiredMode.REQUIRED, example = "PT30S")
    private Duration ntpCheckInterval;

    @Schema(description = "Number of NTP samples to take per server during each check", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    private int ntpSamplesPerServer;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Timeout for a single NTP check, in ISO 8601 duration format", requiredMode = Schema.RequiredMode.REQUIRED, example = "PT5S")
    private Duration ntpCheckTimeout;

    @Schema(description = "Minimum number of NTP servers that must be reachable for the check to be valid", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private int ntpServersMinReachable;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Maximum allowed clock drift from the NTP reference time, in ISO 8601 duration format", requiredMode = Schema.RequiredMode.REQUIRED, example = "PT1S")
    private Duration maxClockDrift;

    @Schema(description = "Whether to guard against leap second anomalies; when true, conflicting leap indicators cause DEGRADED status", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private boolean leapSecondGuard;
}
