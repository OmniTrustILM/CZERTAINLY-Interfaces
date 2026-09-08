package com.otilm.api.model.client.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.otilm.api.model.core.acme.AcmeProfileDto;
import com.otilm.api.model.core.protocol.ProtocolChallengeSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AcmeProfileChallengeSourceTest {

    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

    @Test
    void createRequestReadsTheEnumCode() throws Exception {
        AcmeProfileRequestDto dto = mapper
                .readValue("{\"name\":\"p\",\"challengeSource\":\"certificateRegistration\"}",
                        AcmeProfileRequestDto.class);

        assertEquals(ProtocolChallengeSource.CERTIFICATE_REGISTRATION, dto.getChallengeSource());
        assertTrue(dto.toString().contains("challengeSource=CERTIFICATE_REGISTRATION"));
    }

    @Test
    void createRequestLeavesTheSourceUnsetWhenOmitted() throws Exception {
        AcmeProfileRequestDto dto = mapper.readValue("{\"name\":\"p\"}", AcmeProfileRequestDto.class);

        assertNull(dto.getChallengeSource(), "core applies the protocolDefault fallback, not the contract");
    }

    @Test
    void editRequestReadsTheEnumCode() throws Exception {
        AcmeProfileEditRequestDto dto = mapper
                .readValue("{\"challengeSource\":\"protocolDefault\"}", AcmeProfileEditRequestDto.class);

        assertEquals(ProtocolChallengeSource.PROTOCOL_DEFAULT, dto.getChallengeSource());
        assertTrue(dto.toString().contains("challengeSource=PROTOCOL_DEFAULT"));
    }

    @Test
    void detailSerializesTheEnumCode() throws Exception {
        AcmeProfileDto dto = new AcmeProfileDto();
        dto.setChallengeSource(ProtocolChallengeSource.CERTIFICATE_REGISTRATION);

        assertTrue(mapper.writeValueAsString(dto).contains("\"challengeSource\":\"certificateRegistration\""));
        assertTrue(dto.toString().contains("challengeSource=CERTIFICATE_REGISTRATION"));
    }
}
