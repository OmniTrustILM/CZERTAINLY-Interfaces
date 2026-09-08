package com.otilm.api.model.core.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.otilm.api.model.common.UuidDto;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientCertificateDataResponseDtoTest {

    private static final ObjectMapper MAPPER = JsonMapper.builder().findAndAddModules().build();

    @Test
    void warningsDefaultToAnEmptyListRatherThanNull() {
        assertEquals(List.of(), new ClientCertificateDataResponseDto().getRequestAttributeWarnings());
    }

    @Test
    void toStringIncludesTheWarnings() {
        ClientCertificateDataResponseDto dto = new ClientCertificateDataResponseDto();
        dto.setUuid(UUID.randomUUID().toString());
        dto.setRequestAttributeWarnings(List.of("warning-sentinel"));

        assertTrue(dto.toString().contains("warning-sentinel"));
    }

    @Test
    void eabFieldsAreOmittedFromJsonUntilSet() throws Exception {
        ClientCertificateDataResponseDto dto = new ClientCertificateDataResponseDto();
        dto.setUuid(UUID.randomUUID().toString());

        String json = MAPPER.writeValueAsString(dto);

        assertFalse(json.contains("eabKid"), "issue/renew/rekey responses must not advertise ACME credential fields");
        assertFalse(json.contains("eabHmacKey"));
    }

    @Test
    void eabFieldsSerializeWhenSet() throws Exception {
        String kid = UUID.randomUUID().toString();
        ClientCertificateDataResponseDto dto = new ClientCertificateDataResponseDto();
        dto.setUuid(kid);
        dto.setEabKid(kid);
        dto.setEabHmacKey("a2V5LXNlbnRpbmVs");

        String json = MAPPER.writeValueAsString(dto);

        assertTrue(json.contains("\"eabKid\":\"" + kid + "\""));
        assertTrue(json.contains("\"eabHmacKey\":\"a2V5LXNlbnRpbmVs\""));
    }

    @Test
    void toStringOmitsTheEabHmacKey() {
        ClientCertificateDataResponseDto dto = new ClientCertificateDataResponseDto();
        dto.setUuid(UUID.randomUUID().toString());
        dto.setEabKid("kid-sentinel");
        dto.setEabHmacKey("key-sentinel");

        String rendered = dto.toString();

        assertTrue(rendered.contains("kid-sentinel"));
        assertFalse(rendered.contains("key-sentinel"), "the EAB HMAC key is a credential and must not be logged");
    }

    @Test
    void toLogDataOmitsTheWarnings() {
        UUID uuid = UUID.randomUUID();
        ClientCertificateDataResponseDto dto = new ClientCertificateDataResponseDto();
        dto.setUuid(uuid.toString());
        dto.setRequestAttributeWarnings(List.of("warning-sentinel"));

        Serializable logData = dto.toLogData();

        // An audit record must not carry a variable-length warning list.
        assertInstanceOf(UuidDto.class, logData);
        assertFalse(logData.toString().contains("warning-sentinel"));
        assertEquals(uuid.toString(), ((UuidDto) logData).getUuid());
    }
}
