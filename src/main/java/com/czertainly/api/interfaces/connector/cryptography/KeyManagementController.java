package com.czertainly.api.interfaces.connector.cryptography;

import com.czertainly.api.exception.NotFoundException;
import com.czertainly.api.exception.ValidationException;
import com.czertainly.api.interfaces.AuthProtectedConnectorController;
import com.czertainly.api.model.client.attribute.RequestAttribute;
import com.czertainly.api.model.common.attribute.common.BaseAttribute;
import com.czertainly.api.model.connector.cryptography.key.CreateKeyRequestDto;
import com.czertainly.api.model.connector.cryptography.key.ExportKeyRequestDto;
import com.czertainly.api.model.connector.cryptography.key.ExportKeyResponseDto;
import com.czertainly.api.model.connector.cryptography.key.ImportKeyPairRequestDto;
import com.czertainly.api.model.connector.cryptography.key.KeyDataResponseDto;
import com.czertainly.api.model.connector.cryptography.key.KeyPairDataResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/cryptographyProvider/tokens/{uuid}/keys")
@Tag(
        name = "Key Management",
        description = "Key Management API for cryptographic key management operations. Each key is managed by a specific token that is connected through the Token instance."
)
public interface KeyManagementController extends AuthProtectedConnectorController {

    @Operation(
            summary = "List of Attributes to create a Secret Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of Attributes retrieved"
                    )
            })
    @GetMapping(
            path = "/secret/attributes",
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance not found
     */
    List<BaseAttribute> listCreateSecretKeyAttributes(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid
    ) throws NotFoundException;

    @Operation(
            summary = "Validate list of Attributes to create a Secret Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Attributes validated"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @PostMapping(
            path = "/secret/attributes/validate",
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    @ResponseStatus(
            value = HttpStatus.NO_CONTENT
    )
    /**
     * @throws NotFoundException Token instance not found
     * @throws ValidationException Invalid Attributes
     */
    void validateCreateSecretKeyAttributes(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @RequestBody List<RequestAttribute> attributes
    ) throws NotFoundException, ValidationException;

    @Operation(
            summary = "Create a Secret Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Key created"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @PostMapping(
            path = "/secret",
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance not found
     */
    KeyDataResponseDto createSecretKey(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @RequestBody CreateKeyRequestDto request
    ) throws NotFoundException;

    @Operation(
            summary = "List of Attributes to create a Key Pair"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of Attributes retrieved"
                    )
            })
    @GetMapping(
            path = "/pair/attributes",
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance not found
     */
    List<BaseAttribute> listCreateKeyPairAttributes(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid
    ) throws NotFoundException;

    @Operation(
            summary = "Validate list of Attributes to create a Key Pair"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Attributes validated"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @PostMapping(
            path = "/pair/attributes/validate",
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    @ResponseStatus(
            value = HttpStatus.NO_CONTENT
    )
    /**
     * @throws NotFoundException Token instance not found
     * @throws ValidationException Invalid Attributes
     */
    void validateCreateKeyPairAttributes(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @RequestBody List<RequestAttribute> attributes
    ) throws NotFoundException, ValidationException;

    @Operation(
            summary = "Create a Key Pair, Public and Private Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Key Pair created"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @PostMapping(
            path = "/pair",
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance not found
     */
    KeyPairDataResponseDto createKeyPair(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @RequestBody CreateKeyRequestDto request
    ) throws NotFoundException;

    @Operation(
            summary = "List Keys for the Token instance"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Keys listed"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @GetMapping(
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance not found
     */
    List<KeyDataResponseDto> listKeys(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid
    ) throws NotFoundException;

    @Operation(
            summary = "Get details about the Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Key data retrieved"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @GetMapping(
            path = "/{keyUuid}",
            produces = {"application/json"}
    )
    /**
     * @throws NotFoundException Token instance or Key not found
     */
    KeyDataResponseDto getKey(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @Parameter(description = "Key UUID") @PathVariable String keyUuid
    ) throws NotFoundException;

    @Operation(
            summary = "Destroy a Key"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Key destroyed"
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            ))
            })
    @DeleteMapping(
            path = "/{keyUuid}"
    )
    @ResponseStatus(
            value = HttpStatus.NO_CONTENT
    )
    /**
     * @throws NotFoundException Token instance or Key not found
     */
    void destroyKey(
            @Parameter(description = "Token instance UUID") @PathVariable String uuid,
            @Parameter(description = "Key UUID") @PathVariable String keyUuid
    ) throws NotFoundException;

    // ── IMPORT (optional) ─────────────────────────────────────────────────────
    //
    // Key pair import is an optional capability. The KEY_IMPORT FeatureFlag (v2 Info) lets the Connector express the support.

    @Operation(
            summary = "List of Attributes to import a Cryptographic Key Pair",
            description = "This endpoint is optional. Connectors that do not support key pair import must return HTTP 501."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of Attributes retrieved"),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key pair import.")
            })
    @GetMapping(path = "/import/attributes", produces = {MediaType.APPLICATION_JSON_VALUE})
    List<BaseAttribute> listImportKeyPairAttributes(@Parameter(description = "Token instance UUID") @PathVariable String uuid) throws NotFoundException;

    @Operation(
            summary = "Validate list of Attributes to import a Cryptographic Key Pair",
            description = "This endpoint is optional. Connectors that do not support key pair import must return HTTP 501."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Attributes validated"),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            )),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key pair import.")
            })
    @PostMapping(path = "/import/attributes/validate", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void validateImportKeyPairAttributes(@Parameter(description = "Token instance UUID") @PathVariable String uuid,
                                         @RequestBody List<RequestAttribute> attributes) throws NotFoundException, ValidationException;

    @Operation(
            summary = "Import a Cryptographic Key Pair into the token",
            description = "This endpoint is optional. Connectors that do not support key pair import must return HTTP 501."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Key pair imported"),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            )),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key pair import.")
            })
    @PostMapping(path = "/import", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    KeyPairDataResponseDto importKeyPair(@Parameter(description = "Token instance UUID") @PathVariable String uuid,
                                         @RequestBody ImportKeyPairRequestDto request) throws NotFoundException, ValidationException;

    // ── EXPORT (optional) ─────────────────────────────────────────────────────
    //
    // Key export is an optional capability. The KEY_EXPORT FeatureFlag (v2 Info) lets the Connector express the support.

    @Operation(
            summary = "List of Attributes to export a Cryptographic Key",
            description = "This endpoint is optional. Connectors that do not support key export must return HTTP 501."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of Attributes retrieved"),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key export.")
            })
    @GetMapping(path = "/{keyUuid}/export/attributes", produces = {MediaType.APPLICATION_JSON_VALUE})
    List<BaseAttribute> listExportKeyAttributes(@Parameter(description = "Token instance UUID") @PathVariable String uuid,
                                                @Parameter(description = "Key UUID") @PathVariable String keyUuid) throws NotFoundException;

    @Operation(
            summary = "Validate list of Attributes to export a Cryptographic Key",
            description = "This endpoint is optional. Connectors that do not support key export must return HTTP 501."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Attributes validated"),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            )),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key export.")
            })
    @PostMapping(path = "/{keyUuid}/export/attributes/validate", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void validateExportKeyAttributes(@Parameter(description = "Token instance UUID") @PathVariable String uuid,
                                     @Parameter(description = "Key UUID") @PathVariable String keyUuid,
                                     @RequestBody List<RequestAttribute> attributes) throws NotFoundException, ValidationException;

    @Operation(
            summary = "Export the private key of a Cryptographic Key Pair as raw key material",
            description = "This endpoint is optional. Connectors that do not support key export must return HTTP 501. " +
                    "Only the private key is returned (PrivateKeyInfo); the platform already holds the public key via " +
                    "the certificate and assembles the final PKCS#12 bundle itself."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Private key exported"),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    examples = {@ExampleObject(value = "[\"Error Message 1\",\"Error Message 2\"]")}
                            )),
                    @ApiResponse(responseCode = "501", description = "Not implemented — this connector does not support key export.")
            })
    @PostMapping(path = "/{keyUuid}/export", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ExportKeyResponseDto exportKey(@Parameter(description = "Token instance UUID") @PathVariable String uuid,
                                   @Parameter(description = "Key UUID") @PathVariable String keyUuid,
                                   @RequestBody ExportKeyRequestDto request) throws NotFoundException, ValidationException;

}
