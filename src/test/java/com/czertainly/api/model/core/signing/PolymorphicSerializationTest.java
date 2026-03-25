package com.czertainly.api.model.core.signing;

import com.czertainly.api.model.common.NameAndUuidDto;
import com.czertainly.api.model.common.enums.cryptography.DigestAlgorithm;
import com.czertainly.api.model.core.signing.signatureprofile.DelegatedSigningDto;
import com.czertainly.api.model.core.signing.signatureprofile.DelegatedSigningRequestDto;
import com.czertainly.api.model.core.signing.signatureprofile.ManagedSigningDto;
import com.czertainly.api.model.core.signing.signatureprofile.ManagedSigningRequestDto;
import com.czertainly.api.model.core.signing.signatureprofile.ManagedSigningType;
import com.czertainly.api.model.core.signing.signatureprofile.OneTimeKeyManagedSigningDto;
import com.czertainly.api.model.core.signing.signatureprofile.OneTimeKeyManagedSigningRequestDto;
import com.czertainly.api.model.core.signing.signatureprofile.SigningScheme;
import com.czertainly.api.model.core.signing.signatureprofile.SigningSchemeDto;
import com.czertainly.api.model.core.signing.signatureprofile.SigningSchemeRequestDto;
import com.czertainly.api.model.core.signing.signatureprofile.StaticKeyManagedSigningDto;
import com.czertainly.api.model.core.signing.signatureprofile.StaticKeyManagedSigningRequestDto;
import com.czertainly.api.model.core.signing.workflow.SigningWorkflowConfigurationCreateRequestDto;
import com.czertainly.api.model.core.signing.workflow.SigningWorkflowConfigurationDto;
import com.czertainly.api.model.core.signing.workflow.SigningWorkflowConfigurationUpdateRequestDto;
import com.czertainly.api.model.core.signing.workflow.SigningWorkflowType;
import com.czertainly.api.model.core.signing.workflow.TimestampingConfigurationCreateRequestDto;
import com.czertainly.api.model.core.signing.workflow.TimestampingConfigurationDto;
import com.czertainly.api.model.core.signing.workflow.TimestampingConfigurationUpdateRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PolymorphicSerializationTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // -------------------------------------------------------------------------
    // TimestampingConfigurationDto
    // -------------------------------------------------------------------------

    @Test
    void timestampingConfigurationDto_serializesDiscriminator() throws Exception {
        TimestampingConfigurationDto dto = new TimestampingConfigurationDto();
        dto.setQualifiedTimestamp(true);

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningWorkflowType.Codes.TIMESTAMPING, json.get("type").asText());
    }

    @Test
    void timestampingConfigurationDto_deserializesViaBaseClass() throws Exception {
        String json = """
                {
                  "uuid": "11111111-0000-0000-0000-000000000001",
                  "name": "My TSA Workflow",
                  "type": "timestamping",
                  "qualifiedTimestamp": true,
                  "defaultPolicyId": "1.2.3.4.5",
                  "allowedPolicyIds": ["1.2.3.4.5", "1.2.3.4.6"],
                  "allowedDigestAlgorithms": ["SHA-256", "SHA-512"]
                }
                """;

        SigningWorkflowConfigurationDto base = mapper.readValue(json, SigningWorkflowConfigurationDto.class);

        assertInstanceOf(TimestampingConfigurationDto.class, base);
        TimestampingConfigurationDto result = (TimestampingConfigurationDto) base;
        assertEquals(SigningWorkflowType.TIMESTAMPING, result.getType());
        assertEquals("11111111-0000-0000-0000-000000000001", result.getUuid());
        assertEquals("My TSA Workflow", result.getName());
        assertTrue(result.getQualifiedTimestamp());
        assertEquals("1.2.3.4.5", result.getDefaultPolicyId());
        assertEquals(List.of("1.2.3.4.5", "1.2.3.4.6"), result.getAllowedPolicyIds());
        assertEquals(List.of(DigestAlgorithm.SHA_256, DigestAlgorithm.SHA_512), result.getAllowedDigestAlgorithms());
    }

    @Test
    void timestampingConfigurationDto_roundTrip() throws Exception {
        TimestampingConfigurationDto original = new TimestampingConfigurationDto();
        original.setUuid("11111111-0000-0000-0000-000000000001");
        original.setName("My TSA Workflow");
        original.setQualifiedTimestamp(false);
        original.setDefaultPolicyId("1.2.3.4.5");
        original.setAllowedPolicyIds(List.of("1.2.3.4.5", "1.2.3.4.6"));
        original.setAllowedDigestAlgorithms(List.of(DigestAlgorithm.SHA_256, DigestAlgorithm.SHA_384));

        String json = mapper.writeValueAsString(original);
        SigningWorkflowConfigurationDto deserialized = mapper.readValue(json, SigningWorkflowConfigurationDto.class);

        assertInstanceOf(TimestampingConfigurationDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // TimestampingConfigurationCreateRequestDto
    // -------------------------------------------------------------------------

    @Test
    void timestampingCreateRequestDto_serializesDiscriminator() throws Exception {
        TimestampingConfigurationCreateRequestDto dto = new TimestampingConfigurationCreateRequestDto();
        dto.setName("New Workflow");
        dto.setQualifiedTimestamp(true);

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningWorkflowType.Codes.TIMESTAMPING, json.get("type").asText());
    }

    @Test
    void timestampingCreateRequestDto_deserializesViaBaseClass() throws Exception {
        String json = """
                {
                  "name": "New TSA Workflow",
                  "type": "timestamping",
                  "qualifiedTimestamp": true,
                  "defaultPolicyId": "1.2.3.4.5",
                  "allowedDigestAlgorithms": ["SHA-256"]
                }
                """;

        SigningWorkflowConfigurationCreateRequestDto base =
                mapper.readValue(json, SigningWorkflowConfigurationCreateRequestDto.class);

        assertInstanceOf(TimestampingConfigurationCreateRequestDto.class, base);
        TimestampingConfigurationCreateRequestDto result = (TimestampingConfigurationCreateRequestDto) base;
        assertEquals(SigningWorkflowType.TIMESTAMPING, result.getType());
        assertEquals("New TSA Workflow", result.getName());
        assertTrue(result.getQualifiedTimestamp());
        assertEquals("1.2.3.4.5", result.getDefaultPolicyId());
        assertEquals(List.of(DigestAlgorithm.SHA_256), result.getAllowedDigestAlgorithms());
    }

    @Test
    void timestampingCreateRequestDto_roundTrip() throws Exception {
        TimestampingConfigurationCreateRequestDto original = new TimestampingConfigurationCreateRequestDto();
        original.setName("New TSA Workflow");
        original.setQualifiedTimestamp(true);
        original.setDefaultPolicyId("1.2.3.4.5");
        original.setAllowedPolicyIds(List.of("1.2.3.4.5"));
        original.setAllowedDigestAlgorithms(List.of(DigestAlgorithm.SHA_256, DigestAlgorithm.SHA_512));
        UUID tqUuid = UUID.randomUUID();
        original.setTimeQualityConfigurationUuid(tqUuid);

        String json = mapper.writeValueAsString(original);
        SigningWorkflowConfigurationCreateRequestDto deserialized = mapper.readValue(json, SigningWorkflowConfigurationCreateRequestDto.class);

        assertInstanceOf(TimestampingConfigurationCreateRequestDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // TimestampingConfigurationUpdateRequestDto
    // -------------------------------------------------------------------------

    @Test
    void timestampingUpdateRequestDto_serializesDiscriminator() throws Exception {
        TimestampingConfigurationUpdateRequestDto dto = new TimestampingConfigurationUpdateRequestDto();
        dto.setName("Updated Workflow");
        dto.setQualifiedTimestamp(false);

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningWorkflowType.Codes.TIMESTAMPING, json.get("type").asText());
    }

    @Test
    void timestampingUpdateRequestDto_roundTrip() throws Exception {
        TimestampingConfigurationUpdateRequestDto original = new TimestampingConfigurationUpdateRequestDto();
        original.setName("Updated TSA Workflow");
        original.setQualifiedTimestamp(false);
        original.setAllowedDigestAlgorithms(List.of(DigestAlgorithm.SHA_384));

        String json = mapper.writeValueAsString(original);
        SigningWorkflowConfigurationUpdateRequestDto deserialized = mapper.readValue(json, SigningWorkflowConfigurationUpdateRequestDto.class);

        assertInstanceOf(TimestampingConfigurationUpdateRequestDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeDto — StaticKeyManagedSigningDto
    // -------------------------------------------------------------------------

    @Test
    void staticKeyManagedSigningDto_serializesBothDiscriminators() throws Exception {
        StaticKeyManagedSigningDto dto = new StaticKeyManagedSigningDto();
        dto.setTokenProfile(new NameAndUuidDto("aaaa-bbbb", "Token"));
        dto.setCryptographicKey(new NameAndUuidDto("cccc-dddd", "Key"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.MANAGED, json.get("signingScheme").asText());
        assertEquals(ManagedSigningType.Codes.STATIC_KEY, json.get("managedSigningType").asText());
    }

    @Test
    void staticKeyManagedSigningDto_deserializesViaSigningSchemeBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "staticKey",
                  "tokenProfile": {"uuid": "aaaa-bbbb", "name": "Token"},
                  "cryptographicKey": {"uuid": "cccc-dddd", "name": "Key"}
                }
                """;

        SigningSchemeDto base = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(StaticKeyManagedSigningDto.class, base);
        StaticKeyManagedSigningDto result = (StaticKeyManagedSigningDto) base;
        assertEquals(SigningScheme.MANAGED, result.getSigningScheme());
        assertEquals(ManagedSigningType.STATIC_KEY, result.getManagedSigningType());
        assertEquals("aaaa-bbbb", result.getTokenProfile().getUuid());
        assertEquals("cccc-dddd", result.getCryptographicKey().getUuid());
    }

    @Test
    void staticKeyManagedSigningDto_deserializesViaManagedSigningBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "staticKey",
                  "tokenProfile": {"uuid": "aaaa-bbbb", "name": "Token"},
                  "cryptographicKey": {"uuid": "cccc-dddd", "name": "Key"}
                }
                """;

        ManagedSigningDto base = mapper.readValue(json, ManagedSigningDto.class);

        assertInstanceOf(StaticKeyManagedSigningDto.class, base);
        StaticKeyManagedSigningDto result = (StaticKeyManagedSigningDto) base;
        assertEquals(ManagedSigningType.STATIC_KEY, result.getManagedSigningType());
        assertEquals("aaaa-bbbb", result.getTokenProfile().getUuid());
        assertEquals("cccc-dddd", result.getCryptographicKey().getUuid());
    }

    @Test
    void staticKeyManagedSigningDto_roundTrip() throws Exception {
        StaticKeyManagedSigningDto original = new StaticKeyManagedSigningDto();
        original.setTokenProfile(new NameAndUuidDto("aaaa-bbbb", "Token Profile"));
        original.setCryptographicKey(new NameAndUuidDto("cccc-dddd", "RSA Key"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeDto deserialized = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(StaticKeyManagedSigningDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeDto — OneTimeKeyManagedSigningDto
    // -------------------------------------------------------------------------

    @Test
    void oneTimeKeyManagedSigningDto_serializesBothDiscriminators() throws Exception {
        OneTimeKeyManagedSigningDto dto = new OneTimeKeyManagedSigningDto();
        dto.setRaProfile(new NameAndUuidDto("1111-2222", "RA Profile"));
        dto.setCsrTemplate(new NameAndUuidDto("3333-4444", "CSR Template"));
        dto.setTokenProfile(new NameAndUuidDto("5555-6666", "Token"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.MANAGED, json.get("signingScheme").asText());
        assertEquals(ManagedSigningType.Codes.ONE_TIME_KEY, json.get("managedSigningType").asText());
    }

    @Test
    void oneTimeKeyManagedSigningDto_deserializesViaSigningSchemeBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "oneTimeKey",
                  "raProfile": {"uuid": "1111-2222", "name": "RA Profile"},
                  "csrTemplate": {"uuid": "3333-4444", "name": "CSR Template"},
                  "tokenProfile": {"uuid": "5555-6666", "name": "Token"}
                }
                """;

        SigningSchemeDto base = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningDto.class, base);
        OneTimeKeyManagedSigningDto result = (OneTimeKeyManagedSigningDto) base;
        assertEquals(SigningScheme.MANAGED, result.getSigningScheme());
        assertEquals(ManagedSigningType.ONE_TIME_KEY, result.getManagedSigningType());
        assertEquals("1111-2222", result.getRaProfile().getUuid());
        assertEquals("3333-4444", result.getCsrTemplate().getUuid());
        assertEquals("5555-6666", result.getTokenProfile().getUuid());
    }

    @Test
    void oneTimeKeyManagedSigningDto_deserializesViaManagedSigningBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "oneTimeKey",
                  "raProfile": {"uuid": "1111-2222", "name": "RA Profile"},
                  "csrTemplate": {"uuid": "3333-4444", "name": "CSR Template"},
                  "tokenProfile": {"uuid": "5555-6666", "name": "Token"}
                }
                """;

        ManagedSigningDto base = mapper.readValue(json, ManagedSigningDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningDto.class, base);
        OneTimeKeyManagedSigningDto result = (OneTimeKeyManagedSigningDto) base;
        assertEquals(ManagedSigningType.ONE_TIME_KEY, result.getManagedSigningType());
        assertEquals("1111-2222", result.getRaProfile().getUuid());
        assertEquals("3333-4444", result.getCsrTemplate().getUuid());
        assertEquals("5555-6666", result.getTokenProfile().getUuid());
    }

    @Test
    void oneTimeKeyManagedSigningDto_roundTrip() throws Exception {
        OneTimeKeyManagedSigningDto original = new OneTimeKeyManagedSigningDto();
        original.setRaProfile(new NameAndUuidDto("1111-2222", "RA Profile"));
        original.setCsrTemplate(new NameAndUuidDto("3333-4444", "CSR Template"));
        original.setTokenProfile(new NameAndUuidDto("5555-6666", "Token Profile"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeDto deserialized = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeDto — DelegatedSigningDto
    // -------------------------------------------------------------------------

    @Test
    void delegatedSigningDto_serializesDiscriminator() throws Exception {
        DelegatedSigningDto dto = new DelegatedSigningDto();
        dto.setConnector(new NameAndUuidDto("eeee-ffff", "Connector"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.DELEGATED, json.get("signingScheme").asText());
    }

    @Test
    void delegatedSigningDto_deserializesViaBaseClass() throws Exception {
        String json = """
                {
                  "signingScheme": "delegated",
                  "connector": {"uuid": "eeee-ffff", "name": "My Connector"}
                }
                """;

        SigningSchemeDto base = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(DelegatedSigningDto.class, base);
        DelegatedSigningDto result = (DelegatedSigningDto) base;
        assertEquals(SigningScheme.DELEGATED, result.getSigningScheme());
        assertEquals("eeee-ffff", result.getConnector().getUuid());
    }

    @Test
    void delegatedSigningDto_roundTrip() throws Exception {
        DelegatedSigningDto original = new DelegatedSigningDto();
        original.setConnector(new NameAndUuidDto("eeee-ffff", "My Connector"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeDto deserialized = mapper.readValue(json, SigningSchemeDto.class);

        assertInstanceOf(DelegatedSigningDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeRequestDto — StaticKeyManagedSigningRequestDto
    // -------------------------------------------------------------------------

    @Test
    void staticKeyManagedSigningRequestDto_serializesBothDiscriminators() throws Exception {
        StaticKeyManagedSigningRequestDto dto = new StaticKeyManagedSigningRequestDto();
        dto.setTokenProfileUuid(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        dto.setCryptographicKeyUuid(UUID.fromString("22222222-2222-2222-2222-222222222222"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.MANAGED, json.get("signingScheme").asText());
        assertEquals(ManagedSigningType.Codes.STATIC_KEY, json.get("managedSigningType").asText());
    }

    @Test
    void staticKeyManagedSigningRequestDto_deserializesViaSigningSchemeBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "staticKey",
                  "tokenProfileUuid": "11111111-1111-1111-1111-111111111111",
                  "cryptographicKeyUuid": "22222222-2222-2222-2222-222222222222"
                }
                """;

        SigningSchemeRequestDto base = mapper.readValue(json, SigningSchemeRequestDto.class);

        assertInstanceOf(StaticKeyManagedSigningRequestDto.class, base);
        StaticKeyManagedSigningRequestDto result = (StaticKeyManagedSigningRequestDto) base;
        assertEquals(SigningScheme.MANAGED, result.getSigningScheme());
        assertEquals(ManagedSigningType.STATIC_KEY, result.getManagedSigningType());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), result.getTokenProfileUuid());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), result.getCryptographicKeyUuid());
    }

    @Test
    void staticKeyManagedSigningRequestDto_deserializesViaManagedSigningBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "staticKey",
                  "tokenProfileUuid": "11111111-1111-1111-1111-111111111111",
                  "cryptographicKeyUuid": "22222222-2222-2222-2222-222222222222"
                }
                """;

        ManagedSigningRequestDto base = mapper.readValue(json, ManagedSigningRequestDto.class);

        assertInstanceOf(StaticKeyManagedSigningRequestDto.class, base);
        StaticKeyManagedSigningRequestDto result = (StaticKeyManagedSigningRequestDto) base;
        assertEquals(ManagedSigningType.STATIC_KEY, result.getManagedSigningType());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), result.getTokenProfileUuid());
        assertEquals(UUID.fromString("22222222-2222-2222-2222-222222222222"), result.getCryptographicKeyUuid());
    }

    @Test
    void staticKeyManagedSigningRequestDto_roundTrip() throws Exception {
        StaticKeyManagedSigningRequestDto original = new StaticKeyManagedSigningRequestDto();
        original.setTokenProfileUuid(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        original.setCryptographicKeyUuid(UUID.fromString("22222222-2222-2222-2222-222222222222"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeRequestDto deserialized = mapper.readValue(json, SigningSchemeRequestDto.class);

        assertInstanceOf(StaticKeyManagedSigningRequestDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeRequestDto — OneTimeKeyManagedSigningRequestDto
    // -------------------------------------------------------------------------

    @Test
    void oneTimeKeyManagedSigningRequestDto_serializesBothDiscriminators() throws Exception {
        OneTimeKeyManagedSigningRequestDto dto = new OneTimeKeyManagedSigningRequestDto();
        dto.setRaProfileUuid(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        dto.setCsrTemplateUuid(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        dto.setTokenProfileUuid(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.MANAGED, json.get("signingScheme").asText());
        assertEquals(ManagedSigningType.Codes.ONE_TIME_KEY, json.get("managedSigningType").asText());
    }

    @Test
    void oneTimeKeyManagedSigningRequestDto_deserializesViaSigningSchemeBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "oneTimeKey",
                  "raProfileUuid": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                  "csrTemplateUuid": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                  "tokenProfileUuid": "cccccccc-cccc-cccc-cccc-cccccccccccc"
                }
                """;

        SigningSchemeRequestDto base = mapper.readValue(json, SigningSchemeRequestDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningRequestDto.class, base);
        OneTimeKeyManagedSigningRequestDto result = (OneTimeKeyManagedSigningRequestDto) base;
        assertEquals(SigningScheme.MANAGED, result.getSigningScheme());
        assertEquals(ManagedSigningType.ONE_TIME_KEY, result.getManagedSigningType());
        assertEquals(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), result.getRaProfileUuid());
        assertEquals(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"), result.getCsrTemplateUuid());
        assertEquals(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"), result.getTokenProfileUuid());
    }

    @Test
    void oneTimeKeyManagedSigningRequestDto_deserializesViaManagedSigningBase() throws Exception {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "oneTimeKey",
                  "raProfileUuid": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                  "csrTemplateUuid": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                  "tokenProfileUuid": "cccccccc-cccc-cccc-cccc-cccccccccccc"
                }
                """;

        ManagedSigningRequestDto base = mapper.readValue(json, ManagedSigningRequestDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningRequestDto.class, base);
        OneTimeKeyManagedSigningRequestDto result = (OneTimeKeyManagedSigningRequestDto) base;
        assertEquals(ManagedSigningType.ONE_TIME_KEY, result.getManagedSigningType());
        assertEquals(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), result.getRaProfileUuid());
        assertEquals(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"), result.getCsrTemplateUuid());
        assertEquals(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"), result.getTokenProfileUuid());
    }

    @Test
    void oneTimeKeyManagedSigningRequestDto_roundTrip() throws Exception {
        OneTimeKeyManagedSigningRequestDto original = new OneTimeKeyManagedSigningRequestDto();
        original.setRaProfileUuid(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        original.setCsrTemplateUuid(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"));
        original.setTokenProfileUuid(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeRequestDto deserialized = mapper.readValue(json, SigningSchemeRequestDto.class);

        assertInstanceOf(OneTimeKeyManagedSigningRequestDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // SigningSchemeRequestDto — DelegatedSigningRequestDto
    // -------------------------------------------------------------------------

    @Test
    void delegatedSigningRequestDto_serializesDiscriminator() throws Exception {
        DelegatedSigningRequestDto dto = new DelegatedSigningRequestDto();
        dto.setConnectorUuid(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        JsonNode json = mapper.valueToTree(dto);

        assertEquals(SigningScheme.Codes.DELEGATED, json.get("signingScheme").asText());
    }

    @Test
    void delegatedSigningRequestDto_roundTrip() throws Exception {
        DelegatedSigningRequestDto original = new DelegatedSigningRequestDto();
        original.setConnectorUuid(UUID.fromString("33333333-3333-3333-3333-333333333333"));

        String json = mapper.writeValueAsString(original);
        SigningSchemeRequestDto deserialized = mapper.readValue(json, SigningSchemeRequestDto.class);

        assertInstanceOf(DelegatedSigningRequestDto.class, deserialized);
        assertEquals(original, deserialized);
    }

    // -------------------------------------------------------------------------
    // Unknown type discriminator guards
    // -------------------------------------------------------------------------

    @Test
    void unknownSigningWorkflowType_throwsOnDeserialization() {
        String json = """
                {
                  "type": "unknown_workflow_type",
                  "qualifiedTimestamp": true
                }
                """;

        assertThrows(InvalidTypeIdException.class, () -> mapper.readValue(json, SigningWorkflowConfigurationDto.class));
    }

    @Test
    void unknownSigningByType_throwsOnDeserialization() {
        String json = """
                {
                  "signingScheme": "unknown_signing_scheme"
                }
                """;

        assertThrows(InvalidTypeIdException.class, () -> mapper.readValue(json, SigningSchemeDto.class));
    }

    @Test
    void unknownManagedSigningType_throwsOnDeserialization() {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "unknown_managed_type"
                }
                """;

        assertThrows(InvalidTypeIdException.class, () -> mapper.readValue(json, SigningSchemeDto.class));
    }

    @Test
    void unknownManagedSigningType_throwsWhenDeserializingViaManagedBase() {
        String json = """
                {
                  "signingScheme": "managed",
                  "managedSigningType": "unknown_managed_type"
                }
                """;

        assertThrows(InvalidTypeIdException.class, () -> mapper.readValue(json, ManagedSigningDto.class));
    }
}
