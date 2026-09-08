package com.otilm.api.model.client.signing.profile.workflow.timestamp;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * OpenAPI schema for the polymorphic {@link TimestampSourceRequestDto} hierarchy.
 *
 * <p>
 * The Jackson subtype registry stays on {@link TimestampSourceRequestDto}: {@code @JsonSubTypes} on a published union
 * makes swagger-core recompose each subtype as {@code allOf: [$ref union, own fields]}, a cycle no client generator can
 * express.
 * </p>
 */
@Schema(name = "TimestampSourceRequestDto",
        description = "Source of the timestamps a content-signing profile embeds, as supplied on create and update. "
                + "The required type property is the discriminator selecting the source.",
        type = "object", discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = TimestampSourceType.Codes.INTERNAL,
                        schema = InternalTimestampSourceRequestDto.class)},
        oneOf = {InternalTimestampSourceRequestDto.class})
public interface TimestampSourceRequestInterface extends Serializable {

    @Schema(description = "Kind of timestamp source, and the discriminator selecting the fields that accompany it",
            requiredMode = Schema.RequiredMode.REQUIRED, examples = {TimestampSourceType.Codes.INTERNAL})
    TimestampSourceType getType();
}
