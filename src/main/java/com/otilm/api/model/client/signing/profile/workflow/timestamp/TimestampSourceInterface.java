package com.otilm.api.model.client.signing.profile.workflow.timestamp;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * OpenAPI schema for the polymorphic {@link TimestampSourceDto} hierarchy.
 *
 * <p>
 * The Jackson subtype registry stays on {@link TimestampSourceDto}: {@code @JsonSubTypes} on a published union makes
 * swagger-core recompose each subtype as {@code allOf: [$ref union, own fields]}, a cycle no client generator can
 * express.
 * </p>
 */
@Schema(name = "TimestampSourceDto",
        description = "Source of the timestamps a content-signing profile embeds, as currently configured on the "
                + "profile. The required type property is the discriminator selecting the source.",
        type = "object", discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = TimestampSourceType.Codes.INTERNAL,
                        schema = InternalTimestampSourceDto.class)},
        oneOf = {InternalTimestampSourceDto.class})
public interface TimestampSourceInterface extends Serializable {

    @Schema(description = "Kind of timestamp source, and the discriminator selecting the fields that accompany it",
            requiredMode = Schema.RequiredMode.REQUIRED, examples = {TimestampSourceType.Codes.INTERNAL})
    TimestampSourceType getType();
}
