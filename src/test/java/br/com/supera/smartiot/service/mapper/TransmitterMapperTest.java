package br.com.supera.smartiot.service.mapper;

import static br.com.supera.smartiot.domain.TransmitterAsserts.*;
import static br.com.supera.smartiot.domain.TransmitterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransmitterMapperTest {

    private TransmitterMapper transmitterMapper;

    @BeforeEach
    void setUp() {
        transmitterMapper = new TransmitterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransmitterSample1();
        var actual = transmitterMapper.toEntity(transmitterMapper.toDto(expected));
        assertTransmitterAllPropertiesEquals(expected, actual);
    }
}
